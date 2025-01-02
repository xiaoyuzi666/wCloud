package com.medical.imaging.dto.report;

import lombok.Data;

@Data
public class ReportUpdateRequest {
    private String title;
    private String content;
    private String conclusion;
    private String status;
} 