package com.medical.imaging.dto.patient;

import jakarta.validation.constraints.Past;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientUpdateRequest {
    private String name;
    
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
    
    private String sex;
    private String address;
    private String phone;
    private String medicalRecordNumber;
} 