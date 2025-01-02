package com.medical.imaging.dto.image;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class WindowLevelRequest {
    @NotNull
    private Integer windowWidth;
    @NotNull
    private Integer windowCenter;
    private String preset;
} 