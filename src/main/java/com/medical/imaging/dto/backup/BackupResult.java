package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupStatus;
import com.medical.imaging.enums.BackupType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BackupResult {
    private String backupId;
    private BackupStatus status;
    private BackupType type;
    private String backupPath;
    private Long totalSize;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String message;
} 