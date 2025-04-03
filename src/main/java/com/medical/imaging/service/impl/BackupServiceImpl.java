package com.medical.imaging.service.impl;

import com.medical.imaging.dto.backup.*;
import com.medical.imaging.entity.BackupTask;
import com.medical.imaging.entity.BackupConfig;
import com.medical.imaging.enums.BackupStatus;
import com.medical.imaging.enums.BackupType;
import com.medical.imaging.repository.BackupTaskRepository;
import com.medical.imaging.repository.BackupConfigRepository;
import com.medical.imaging.service.BackupService;
import com.medical.imaging.service.BackupTaskExecutor;
import com.medical.imaging.service.EmailService;
import com.medical.imaging.service.SystemConfigService;
import com.medical.imaging.exception.ResourceNotFoundException;
import com.medical.imaging.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.medical.imaging.model.BackupProgress;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

    private final BackupTaskRepository backupRepository;
    private final BackupConfigRepository configRepository;
    private final SystemConfigService systemConfigService;
    private final EmailService emailService;
    private final BackupTaskExecutor backupTaskExecutor;

    private final Map<String, BackupProgress> backupProgressMap = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public BackupTaskDTO startBackup(BackupRequest request) {
        // 验证备份配置
        BackupConfig config = configRepository.findCurrentConfig()
            .orElseThrow(() -> new BusinessException("Backup configuration not found"));

        if (!config.getEnabledTypes().contains(request.getType())) {
            throw new BusinessException("Backup type not enabled: " + request.getType());
        }

        // 检查并发任务数量限制
        long runningTasks = backupRepository.countByStatus(BackupStatus.IN_PROGRESS);
        if (runningTasks >= config.getMaxConcurrentTasks()) {
            throw new BusinessException("Maximum concurrent backup tasks reached");
        }

        // 创建备份任务
        BackupTask task = new BackupTask();
        task.setType(request.getType());
        task.setStatus(BackupStatus.PENDING);
        task.setStartTime(LocalDateTime.now());
        task.setIncludePaths(request.getIncludePaths());
        task.setExcludePaths(request.getExcludePaths());
        task.setCompress(request.getCompress());
        task.setEncrypt(request.getEncrypt());

        task = backupRepository.save(task);

        // 异步执行备份
        backupTaskExecutor.executeBackup(task);

        return convertToTaskDTO(task);
    }

    @Override
    @Transactional
    public void pauseBackup(String backupId) {
        BackupTask task = backupRepository.findById(backupId)
            .orElseThrow(() -> new ResourceNotFoundException("Backup task not found"));

        if (task.getStatus() != BackupStatus.IN_PROGRESS) {
            throw new BusinessException("Can only pause in-progress backups");
        }

        backupTaskExecutor.pauseBackup(task);
        task.setStatus(BackupStatus.PAUSED);
        backupRepository.save(task);
        
        log.info("Backup task paused: {}", backupId);
    }

    @Override
    @Transactional
    public void resumeBackup(String backupId) {
        BackupTask task = backupRepository.findById(backupId)
            .orElseThrow(() -> new ResourceNotFoundException("Backup task not found"));

        if (task.getStatus() != BackupStatus.PAUSED) {
            throw new BusinessException("Can only resume paused backups");
        }

        backupTaskExecutor.resumeBackup(task);
        task.setStatus(BackupStatus.IN_PROGRESS);
        backupRepository.save(task);
        
        log.info("Backup task resumed: {}", backupId);
    }

    @Override
    @Transactional
    public void deleteBackup(String backupId) {
        BackupTask task = backupRepository.findById(backupId)
            .orElseThrow(() -> new ResourceNotFoundException("Backup task not found"));

        if (task.getStatus() == BackupStatus.IN_PROGRESS) {
            throw new BusinessException("Cannot delete an in-progress backup");
        }

        try {
            // 删除备份文件
            if (task.getBackupPath() != null) {
                Files.deleteIfExists(Paths.get(task.getBackupPath()));
            }
            
            // 删除数据库记录
            backupRepository.delete(task);
            
            log.info("Backup task deleted: {}", backupId);
        } catch (IOException e) {
            log.error("Failed to delete backup files", e);
            throw new BusinessException("Failed to delete backup files");
        }
    }

    @Override
    public BackupStatusDTO getBackupStatus(String backupId) {
        BackupTask task = backupRepository.findById(backupId)
            .orElseThrow(() -> new ResourceNotFoundException("Backup task not found"));

        BackupProgress progress = backupProgressMap.get(backupId);
        
        return BackupStatusDTO.builder()
            .status(task.getStatus())
            .progress(progress != null ? progress.getProgress() : null)
            .message(task.getMessage())
            .estimatedCompletionTime(calculateEstimatedCompletionTime(task, progress))
            .processedSize(progress != null ? progress.getProcessedSize() : null)
            .totalSize(task.getTotalSize())
            .build();
    }

    @Override
    public Page<BackupHistoryDTO> getBackupHistory(Pageable pageable) {
        return backupRepository.findAll(pageable)
            .map(this::convertToHistoryDTO);
    }

    @Override
    public BackupConfigDTO getBackupConfig() {
        return configRepository.findCurrentConfig()
            .map(this::convertToConfigDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Backup configuration not found"));
    }

    @Override
    @Transactional
    public void updateBackupConfig(BackupConfigRequest request) {
        BackupConfig config = configRepository.findCurrentConfig()
            .orElse(new BackupConfig());

        config.setBackupPath(request.getBackupPath());
        config.setSchedule(request.getSchedule());
        config.setRetentionDays(request.getRetentionDays());
        config.setMaxConcurrentTasks(request.getMaxConcurrentTasks());
        config.setAutoBackup(request.getAutoBackup());
        config.setEnabledTypes(request.getEnabledTypes());
        config.setAdditionalSettings(request.getAdditionalSettings());

        configRepository.save(config);
        
        log.info("Backup configuration updated");
    }

    @Override
    public BackupStatisticsDTO getBackupStatistics() {
        return BackupStatisticsDTO.builder()
            .totalBackups(backupRepository.count())
            .successfulBackups(backupRepository.countByStatus(BackupStatus.COMPLETED))
            .failedBackups(backupRepository.countByStatus(BackupStatus.FAILED))
            .averageBackupTime(backupRepository.calculateAverageBackupTime())
            .lastBackupTime(backupRepository.findLastSuccessfulBackupTime())
            .totalStorageUsed(backupRepository.calculateTotalStorageUsed())
            .backupsByType(backupRepository.countByBackupType())
            .storageByMonth(backupRepository.calculateStorageByMonth())
            .build();
    }

    private BackupTaskDTO convertToTaskDTO(BackupTask task) {
        return BackupTaskDTO.builder()
            .id(task.getId())
            .status(task.getStatus())
            .type(task.getType())
            .progress(backupProgressMap.get(task.getId()) != null ? 
                backupProgressMap.get(task.getId()).getProgress() : null)
            .message(task.getMessage())
            .startTime(task.getStartTime())
            .endTime(task.getEndTime())
            .totalSize(task.getTotalSize())
            .build();
    }

    private BackupHistoryDTO convertToHistoryDTO(BackupTask task) {
        return BackupHistoryDTO.builder()
            .id(task.getId())
            .status(task.getStatus())
            .type(task.getType())
            .startTime(task.getStartTime())
            .endTime(task.getEndTime())
            .totalSize(task.getTotalSize())
            .createdBy(task.getCreatedBy())
            .message(task.getMessage())
            .build();
    }

    private BackupConfigDTO convertToConfigDTO(BackupConfig config) {
        return BackupConfigDTO.builder()
            .backupPath(config.getBackupPath())
            .schedule(config.getSchedule())
            .retentionDays(config.getRetentionDays())
            .maxConcurrentTasks(config.getMaxConcurrentTasks())
            .autoBackup(config.getAutoBackup())
            .enabledTypes(config.getEnabledTypes())
            .additionalSettings(config.getAdditionalSettings())
            .build();
    }

    private LocalDateTime calculateEstimatedCompletionTime(BackupTask task, BackupProgress progress) {
        if (progress == null || progress.getProgress() == null || progress.getProgress() == 0) {
            return null;
        }

        Duration elapsedTime = Duration.between(task.getStartTime(), LocalDateTime.now());
        long totalSeconds = elapsedTime.getSeconds() * 100 / progress.getProgress();
        long remainingSeconds = totalSeconds - elapsedTime.getSeconds();

        return LocalDateTime.now().plusSeconds(remainingSeconds);
    }

    @Slf4j
    @RequiredArgsConstructor
    private static class BackupProgress {
        private final Integer progress;
        private final Long processedSize;
        private final LocalDateTime lastUpdateTime;

        public Integer getProgress() {
            return progress;
        }

        public Long getProcessedSize() {
            return processedSize;
        }
    }
} 