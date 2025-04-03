package com.medical.imaging.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordResetRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String resetToken;
    @NotBlank
    private String newPassword;
} 