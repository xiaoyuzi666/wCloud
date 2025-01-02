package com.medical.imaging.dto.report;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ReportTemplateRequest {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String content;
    private String category;
    private boolean isDefault;
} 