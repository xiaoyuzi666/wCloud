package com.medical.imaging.entity;

import com.medical.imaging.enums.BackupStatus;
import com.medical.imaging.enums.BackupType;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "backup_tasks")
public class BackupTask {
    @Id
    private String id;
    
    @Enumerated(EnumType.STRING)
    private BackupType type;
    
    @Enumerated(EnumType.STRING)
    private BackupStatus status;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String message;
    private Long totalSize;
    private String backupPath;
    private String createdBy;
    
    @ElementCollection
    private Set<String> includePaths;
    
    @ElementCollection
    private Set<String> excludePaths;
    
    private Boolean compress;
    private Boolean encrypt;
} 