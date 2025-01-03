package com.medical.imaging.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String token;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
} 