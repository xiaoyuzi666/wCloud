package com.medical.imaging.dto.image;

import com.medical.imaging.enums.ImageFormat;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BatchExportRequest {
    @NotEmpty(message = "Instance IDs cannot be empty")
    private List<String> instanceIds;
    private ImageFormat format;
    private Boolean includeAnnotations;
    private String exportPath;
} 