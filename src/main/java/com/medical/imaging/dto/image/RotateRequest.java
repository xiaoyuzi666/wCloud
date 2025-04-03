package com.medical.imaging.dto.image;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RotateRequest {
    @NotNull(message = "Angle cannot be null")
    private Integer angle;
    
    private Boolean applyToSeries;
} 