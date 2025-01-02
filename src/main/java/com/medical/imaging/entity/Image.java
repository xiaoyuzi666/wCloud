package com.medical.imaging.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    
    @Id
    private String instanceId;

    @Column(name = "series_id", nullable = false)
    private String seriesId;

    @Column(name = "study_id", nullable = false)
    private String studyId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String modality;

    @Column(name = "image_type")
    private String imageType;

    @Column(name = "instance_number")
    private Integer instanceNumber;

    @Column(name = "storage_path", nullable = false)
    private String storagePath;

    @Column(name = "file_size")
    private Long fileSize;

    private Integer width;
    private Integer height;

    @Column(name = "bits_allocated")
    private Integer bitsAllocated;

    @Column(name = "transfer_syntax")
    private String transferSyntax;

    @Column(name = "acquisition_datetime")
    private LocalDateTime acquisitionDateTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 