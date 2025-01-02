package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BackupStatisticsDTO {
    private long totalBackups;
    private long successfulBackups;
    private long failedBackups;
    private Duration averageBackupTime;
    private LocalDateTime lastBackupTime;
    private Long totalStorageUsed;
    private Map<BackupType, Long> backupsByType;
    private Map<String, Long> storageByMonth;
} 