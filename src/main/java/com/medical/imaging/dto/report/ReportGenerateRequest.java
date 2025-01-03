package com.medical.imaging.dto.report;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ReportGenerateRequest {
    @NotNull(message = "Study ID cannot be null")
    private Long studyId;
    
    @NotNull(message = "Template ID cannot be null")
    private Long templateId;
    
    private String title;
    private String content;
    private String conclusion;
    private String doctorName;
} 