package com.medical.imaging.dto.backup;

import com.medical.imaging.enums.BackupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BackupRequest {
    @NotNull
    private BackupType type;
    private Set<String> includePaths;
    private Set<String> excludePaths;
    private Boolean compress;
    private Boolean encrypt;
} 