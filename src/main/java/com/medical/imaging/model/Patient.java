package com.medical.imaging.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String patientId;  // 病人ID
    
    private LocalDate birthDate;
    
    @Column(length = 1)
    private String gender;  // M/F
    
    private String phoneNumber;
} 