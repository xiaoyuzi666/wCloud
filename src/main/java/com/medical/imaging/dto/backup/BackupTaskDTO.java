package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupStatus;
import com.medical.imaging.enums.BackupType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BackupTaskDTO {
    private String id;
    private BackupStatus status;
    private BackupType type;
    private Integer progress;
    private String message;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long totalSize;
} 