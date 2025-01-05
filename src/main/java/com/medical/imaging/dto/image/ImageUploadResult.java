package com.medical.imaging.dto.image;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageUploadResult {
    private String uploadId;
    private String instanceId;
    private String status;
    private Integer progress;
    private String message;
} 