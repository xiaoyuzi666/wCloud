package com.medical.imaging.dto.report;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportDTO {
    private Long id;
    private Long studyId;
    private String title;
    private String content;
    private String conclusion;
    private String doctorName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
} 