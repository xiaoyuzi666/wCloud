package com.medical.imaging.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportGenerateRequest {
    @NotNull(message = "Template ID cannot be null")
    private Long templateId;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    private String findings;
    private String conclusion;
    private String recommendation;
    private String doctorName;
} 