package com.medical.imaging.dto.report;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ReportTemplateRequest {
    @NotBlank(message = "Template name cannot be empty")
    private String name;
    private String description;
    @NotBlank(message = "Template content cannot be empty")
    private String content;
    private String category;
    private boolean isDefault;
} 