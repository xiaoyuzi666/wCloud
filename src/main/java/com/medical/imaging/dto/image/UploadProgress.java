package com.medical.imaging.dto.image;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadProgress {
    private String uploadId;
    private String status;
    private Integer progress;
    private String message;
    private Long processedBytes;
    private Long totalBytes;
    private LocalDateTime startTime;
    private LocalDateTime estimatedCompletionTime;
} 