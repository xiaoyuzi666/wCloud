package com.medical.imaging.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "images")
public class Image {
    @Id
    private String instanceId;
    
    @Column(name = "series_id")
    private String seriesId;
    
    @Column(name = "study_id")
    private String studyId;
    
    @Column(name = "patient_id")
    private String patientId;
    
    private String modality;
    
    @Column(name = "image_type")
    private String imageType;
    
    @Column(name = "instance_number")
    private Integer instanceNumber;
    
    @Column(name = "storage_path")
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

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
} 