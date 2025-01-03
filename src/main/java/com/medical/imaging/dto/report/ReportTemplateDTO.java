package com.medical.imaging.dto.report;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportTemplateDTO {
    private Long id;
    private String name;
    private String description;
    private String content;
    private String category;
    private boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
} 