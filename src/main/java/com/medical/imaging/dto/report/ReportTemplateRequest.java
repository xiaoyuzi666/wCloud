package com.medical.imaging.dto.report;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ReportTemplateRequest {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    private String description;
    private String modality;
    @NotBlank(message = "Template content cannot be empty")
    private String template;
    private Boolean isDefault;
} 