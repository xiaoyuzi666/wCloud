package com.medical.imaging.service;

import com.medical.imaging.dto.auth.*;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    TokenResponse refreshToken(String refreshToken);
    void logout(String token);
    void resetPassword(String email);
    void changePassword(String token, String newPassword);
    void verifyResetToken(String token);
    UserProfileResponse getCurrentUser();
} 