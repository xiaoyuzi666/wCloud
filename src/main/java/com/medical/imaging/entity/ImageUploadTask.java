package com.medical.imaging.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "image_upload_tasks")
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadTask {
    
    @Id
    private String uploadId;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(nullable = false)
    private String status;

    private String message;

    private Integer progress;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 