package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupType;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Data
public class BackupRequest {
    @NotNull(message = "Backup type cannot be null")
    private BackupType type;
    
    private Set<String> includePaths;
    private Set<String> excludePaths;
    private Boolean compress;
    private Boolean encrypt;
} 