package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BackupConfigDTO {
    private String backupPath;
    private String schedule;
    private Integer retentionDays;
    private Integer maxConcurrentTasks;
    private Boolean autoBackup;
    private Set<BackupType> enabledTypes;
    private Map<String, String> additionalSettings;
} 