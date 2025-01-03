package com.medical.imaging.dto.image;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class WindowLevelRequest {
    @NotNull(message = "Window width cannot be null")
    private Integer windowWidth;
    
    @NotNull(message = "Window center cannot be null")
    private Integer windowCenter;
    
    private String preset;
} 