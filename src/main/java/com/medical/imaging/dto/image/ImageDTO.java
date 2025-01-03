package com.medical.imaging.dto.image;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ImageDTO {
    private String instanceId;
    private String seriesId;
    private String studyId;
    private String patientId;
    private String modality;
    private String imageType;
    private Integer instanceNumber;
    private String storagePath;
    private Long fileSize;
    private Integer width;
    private Integer height;
    private Integer bitsAllocated;
    private String transferSyntax;
    private LocalDateTime acquisitionDateTime;
    private LocalDateTime createdAt;
} 