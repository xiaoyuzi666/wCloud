package com.medical.imaging.dto.image;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ImageUploadResult {
    private String uploadId;
    private String instanceId;
    private String status;
    private String message;
    private Integer progress;
} 