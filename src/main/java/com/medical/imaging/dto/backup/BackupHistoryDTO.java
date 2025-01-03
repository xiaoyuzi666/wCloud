package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupStatus;
import com.medical.imaging.enums.BackupType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BackupHistoryDTO {
    private String id;
    private BackupStatus status;
    private BackupType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long totalSize;
    private String createdBy;
    private String message;
} 