package com.medical.imaging.dto.report;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ReportUpdateRequest {
    private String title;
    private String content;
    private String conclusion;
    private String doctorName;
    private String status;
} 