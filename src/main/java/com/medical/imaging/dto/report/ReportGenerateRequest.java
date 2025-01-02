package com.medical.imaging.dto.report;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ReportGenerateRequest {
    @NotNull
    private Long studyId;
    @NotNull
    private Long templateId;
    private String title;
    private String content;
    private String conclusion;
    private String doctorName;
} 