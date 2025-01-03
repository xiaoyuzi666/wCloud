package com.medical.imaging.dto.config;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SystemConfigDTO {
    private String key;
    private String value;
    private String description;
    private String category;
    private Boolean encrypted;
    private LocalDateTime lastModified;
    private String modifiedBy;
} 