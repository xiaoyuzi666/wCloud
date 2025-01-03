package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RestoreResult {
    private String restoreId;
    private String backupId;
    private BackupStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String message;
    private Integer progress;
    private Boolean success;
} 