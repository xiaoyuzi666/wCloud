package com.medical.imaging.service;

import com.medical.imaging.dto.backup.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BackupService {
    // 备份操作
    BackupTaskDTO startBackup(BackupRequest request);
    void pauseBackup(String backupId);
    void resumeBackup(String backupId);
    void deleteBackup(String backupId);
    
    // 状态查询
    BackupStatusDTO getBackupStatus(String backupId);
    Page<BackupHistoryDTO> getBackupHistory(Pageable pageable);
    
    // 配置管理
    BackupConfigDTO getBackupConfig();
    void updateBackupConfig(BackupConfigRequest request);
    
    // 统计信息
    BackupStatisticsDTO getBackupStatistics();
} 