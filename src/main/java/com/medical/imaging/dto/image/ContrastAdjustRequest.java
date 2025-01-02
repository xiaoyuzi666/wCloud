package com.medical.imaging.dto.image;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ContrastAdjustRequest {
    @NotNull
    private Double contrast;
    @NotNull
    private Double brightness;
    private Boolean applyToSeries;
} 