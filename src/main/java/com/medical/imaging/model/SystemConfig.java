package com.medical.imaging.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "system_configs")
public class SystemConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_key", unique = true, nullable = false)
    private String configKey;
    
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
    
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
} 