import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordChangeRequest {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
} 