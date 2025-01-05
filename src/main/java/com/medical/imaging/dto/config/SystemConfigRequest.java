package com.medical.imaging.dto.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SystemConfigRequest {
    @NotBlank(message = "Configuration value cannot be empty")
    private String value;
    private String description;
    private String category;
    private Boolean encrypted;
} 