package com.medical.imaging.dto.image;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ContrastAdjustRequest {
    @NotNull(message = "Contrast factor cannot be null")
    private Double factor;
} 