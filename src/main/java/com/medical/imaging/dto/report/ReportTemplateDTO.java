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
    private String modality;
    private String template;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private String createdBy;
} 