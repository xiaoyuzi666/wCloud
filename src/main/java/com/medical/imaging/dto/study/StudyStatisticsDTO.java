package com.medical.imaging.dto.study;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class StudyStatisticsDTO {
    private Long totalStudies;
    private List<DailyStudyCount> studiesByDay;
    private List<PatientStudyCount> studiesByPatient;
} 