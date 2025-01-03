package com.medical.imaging.dto.image;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ContrastAdjustRequest {
    @NotNull(message = "Contrast value cannot be null")
    private Double contrast;
    @NotNull(message = "Brightness value cannot be null")
    private Double brightness;
    private Boolean applyToSeries;
} 