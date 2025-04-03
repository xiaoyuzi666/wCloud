package com.medical.imaging.dto.study;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SeriesDTO {
    private Long id;
    private String seriesInstanceUid;
    private String seriesDescription;
    private String modality;
    private Integer seriesNumber;
    private Integer numberOfInstances;
    private String bodyPart;
    private LocalDateTime seriesDate;
    private String manufacturer;
    private String stationName;
    private LocalDateTime createdAt;
} 