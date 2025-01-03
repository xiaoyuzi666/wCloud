package com.medical.imaging.dto.auth;

import com.medical.imaging.dto.user.UserDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserDTO user;
} 