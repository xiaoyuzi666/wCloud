package com.medical.imaging.dto.study;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class StudyStatisticsDTO {
    private Long totalStudies;
    private Map<String, Long> studiesByModality;
    private Map<String, Long> studiesByDay;
    private Map<String, Long> studiesByStatus;
    private Double averageSeriesPerStudy;
    private Double averageInstancesPerStudy;
    private Long totalStorageSize;
} 