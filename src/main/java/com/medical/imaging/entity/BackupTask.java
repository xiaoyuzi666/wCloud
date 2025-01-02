package com.medical.imaging.entity;

import com.medical.imaging.enums.BackupStatus;
import com.medical.imaging.enums.BackupType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "backup_tasks")
@NoArgsConstructor
@AllArgsConstructor
public class BackupTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BackupType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BackupStatus status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "backup_path")
    private String backupPath;

    @Column(name = "total_size")
    private Long totalSize;

    @ElementCollection
    @CollectionTable(name = "backup_include_paths")
    private Set<String> includePaths;

    @ElementCollection
    @CollectionTable(name = "backup_exclude_paths")
    private Set<String> excludePaths;

    @Column(nullable = false)
    private Boolean compress = false;

    @Column(nullable = false)
    private Boolean encrypt = false;

    @Column(name = "created_by")
    private String createdBy;

    private String message;
} 