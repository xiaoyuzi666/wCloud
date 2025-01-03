package com.medical.imaging.service;

import com.medical.imaging.dto.backup.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BackupService {
    BackupResult createBackup(BackupRequest request);
    RestoreResult restore(String backupId);
    BackupStatus getBackupStatus(String backupId);
    void pauseBackup(String backupId);
    void resumeBackup(String backupId);
    void deleteBackup(String backupId);
    Page<BackupHistoryDTO> getBackupHistory(Pageable pageable);
    BackupConfigDTO getBackupConfig();
    void updateBackupConfig(BackupConfigRequest request);
    BackupStatisticsDTO getBackupStatistics();
} 