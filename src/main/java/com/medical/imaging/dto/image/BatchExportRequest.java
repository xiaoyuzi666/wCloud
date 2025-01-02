package com.medical.imaging.dto.image;

import com.medical.imaging.enums.ImageFormat;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BatchExportRequest {
    @NotEmpty
    private List<String> instanceIds;
    private ImageFormat format;
    private Boolean includeAnnotations;
    private String exportPath;
} 