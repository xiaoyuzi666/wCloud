package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupType;
import lombok.Data;

import java.util.Set;

@Data
public class BackupRequest {
    private BackupType type;
    private Set<String> includePaths;
    private Set<String> excludePaths;
    private Boolean compress;
    private Boolean encrypt;
} 