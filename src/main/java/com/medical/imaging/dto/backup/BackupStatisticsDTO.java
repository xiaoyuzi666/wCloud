package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupType;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class BackupStatisticsDTO {
    private Long totalBackups;
    private Long successfulBackups;
    private Long failedBackups;
    private Duration averageBackupTime;
    private LocalDateTime lastBackupTime;
    private Long totalStorageUsed;
    private Map<BackupType, Long> backupsByType;
    private Map<String, Long> storageByMonth;
} 