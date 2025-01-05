package com.medical.imaging.dto.report;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportDTO {
    private Long id;
    private Long studyId;
    private String content;
    private String status;
    private String conclusion;
    private String findings;
    private String recommendation;
    private String doctorName;
    private Long templateId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
} 