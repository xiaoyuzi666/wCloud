import com.medical.imaging.enums.BackupType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class BackupConfigRequest {
    @NotBlank
    private String backupPath;
    private String schedule;
    @Min(1)
    private Integer retentionDays;
    @Min(1)
    private Integer maxConcurrentTasks;
    private Boolean autoBackup;
    private Set<BackupType> enabledTypes;
    private Map<String, String> additionalSettings;
} 