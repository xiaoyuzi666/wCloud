package com.medical.imaging.dto.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfigUpdateRequest {
    @NotBlank(message = "Value cannot be empty")
    private String value;
} 