package com.medical.imaging.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "image_upload_tasks")
public class ImageUploadTask {
    @Id
    private String uploadId;

    private String status;
    private Integer progress;
    private String message;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "instance_id")
    private String instanceId;
    
    @Column(name = "processed_bytes")
    private Long processedBytes;
    
    @Column(name = "total_bytes")
    private Long totalBytes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
} 