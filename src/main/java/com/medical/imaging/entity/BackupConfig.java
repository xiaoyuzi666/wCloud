package com.medical.imaging.entity;

import com.medical.imaging.enums.BackupType;
import lombok.Data;
import jakarta.persistence.*;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@Table(name = "backup_configs")
public class BackupConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String backupPath;
    private String schedule;
    private Integer retentionDays;
    private Integer maxConcurrentTasks;
    private Boolean autoBackup;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<BackupType> enabledTypes;

    @ElementCollection
    @CollectionTable(name = "backup_config_settings")
    @MapKeyColumn(name = "setting_key")
    @Column(name = "setting_value")
    private Map<String, String> additionalSettings;
} 