package com.medical.imaging.dto.study;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientStudyCount {
    private String patientName;
    private Long count;
} 