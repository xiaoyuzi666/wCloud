package com.medical.imaging.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "study_instance_uid", unique = true)
    private String studyInstanceUid;

    @Column(name = "accession_number")
    private String accessionNumber;

    @Column(name = "study_date")
    private LocalDateTime studyDate;

    private String description;
    private String modality;
    private String status;

    @Column(name = "referring_physician")
    private String referringPhysician;

    @Column(name = "study_description")
    private String studyDescription;

    @Column(name = "number_of_series")
    private Integer numberOfSeries;

    @Column(name = "number_of_instances")
    private Integer numberOfInstances;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
} 