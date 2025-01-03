package com.medical.imaging.dto.patient;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PatientDTO {
    private Long id;
    private String patientId;
    private String name;
    private LocalDate birthDate;
    private String sex;
    private String address;
    private String phone;
    private String medicalRecordNumber;
    private Integer numberOfStudies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 