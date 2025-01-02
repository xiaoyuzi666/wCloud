package com.medical.imaging.entity;

import com.medical.imaging.enums.BackupType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.util.Map;
import java.util.Set;

@Data
@Entity
@Table(name = "backup_config")
@NoArgsConstructor
@AllArgsConstructor
public class BackupConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String backupPath;

    private String schedule;

    @Column(name = "retention_days")
    private Integer retentionDays;

    @Column(name = "max_concurrent_tasks")
    private Integer maxConcurrentTasks;

    @Column(name = "auto_backup")
    private Boolean autoBackup = false;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "backup_enabled_types")
    private Set<BackupType> enabledTypes;

    @ElementCollection
    @CollectionTable(name = "backup_additional_settings")
    @MapKeyColumn(name = "setting_key")
    @Column(name = "setting_value")
    private Map<String, String> additionalSettings;
} 