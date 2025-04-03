package com.medical.imaging.dto.image;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class BatchExportResult {
    private String exportId;
    private Integer totalFiles;
    private Integer processedFiles;
    private String status;
    private String exportPath;
    private List<String> exportedFiles;
    private List<String> failedFiles;
} 