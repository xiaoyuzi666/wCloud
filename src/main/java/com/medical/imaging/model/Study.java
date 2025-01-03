package com.medical.imaging.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "studies")
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @Column(name = "study_date", nullable = false)
    private LocalDateTime studyDate;
    
    @Column(name = "study_description")
    private String studyDescription;
    
    @Column(name = "modality")
    private String modality;
    
    @Column(name = "accession_number")
    private String accessionNumber;
    
    @Column(name = "referring_physician")
    private String referringPhysician;
    
    @Column(nullable = false)
    private String studyInstanceUid;  // DICOM Study Instance UID
    
    @Column(nullable = false)
    private String orthancStudyId;  // Orthanc中的研究ID
} 