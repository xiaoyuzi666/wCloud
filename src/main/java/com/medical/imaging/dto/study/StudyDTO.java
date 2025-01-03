package com.medical.imaging.dto.study;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StudyDTO {
    private Long id;
    private String studyInstanceUid;
    private String accessionNumber;
    private String studyDescription;
    private LocalDateTime studyDate;
    private String modality;
    private String patientId;
    private String patientName;
    private String patientBirthDate;
    private String patientSex;
    private Integer numberOfSeries;
    private Integer numberOfInstances;
    private String studyStatus;
    private String referringPhysician;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 