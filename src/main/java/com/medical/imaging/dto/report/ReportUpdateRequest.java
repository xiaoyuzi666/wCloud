package com.medical.imaging.dto.report;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportUpdateRequest {
    @NotBlank(message = "Content cannot be empty")
    private String content;

    private String findings;
    private String conclusion;
    private String recommendation;
    private String status;
} 