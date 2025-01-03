package com.medical.imaging.dto.image;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class BatchUploadResult {
    private String batchId;
    private Integer totalFiles;
    private Integer successCount;
    private Integer failureCount;
    private String status;
    private List<ImageUploadResult> results;
} 