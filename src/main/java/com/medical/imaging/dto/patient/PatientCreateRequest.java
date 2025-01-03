package com.medical.imaging.dto.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientCreateRequest {
    @NotBlank(message = "Patient ID cannot be empty")
    private String patientId;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String sex;
    private String address;
    private String phone;
    private String medicalRecordNumber;
} 