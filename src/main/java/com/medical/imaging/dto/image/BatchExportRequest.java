package com.medical.imaging.dto.image;

import com.medical.imaging.enums.ImageFormat;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class BatchExportRequest {
    @NotEmpty(message = "Instance IDs cannot be empty")
    private List<String> instanceIds;
    
    @NotNull(message = "Export format cannot be null")
    private ImageFormat format;
    
    @NotNull(message = "Export path cannot be null")
    private String exportPath;
} 