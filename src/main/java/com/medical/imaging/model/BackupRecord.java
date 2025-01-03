package com.medical.imaging.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "backup_records")
public class BackupRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BackupStatus status;
    
    @Column(name = "backup_path")
    private String backupPath;
    
    @Column(name = "total_size")
    private Long totalSize;
    
    @Column(name = "error_message", length = 1000)
    private String errorMessage;
    
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    public enum BackupStatus {
        IN_PROGRESS, COMPLETED, FAILED
    }
} 