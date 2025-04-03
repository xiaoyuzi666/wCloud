package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BackupStatusDTO {
    private BackupStatus status;
    private Integer progress;
    private String message;
    private LocalDateTime estimatedCompletionTime;
    private Long processedSize;
    private Long totalSize;
} 