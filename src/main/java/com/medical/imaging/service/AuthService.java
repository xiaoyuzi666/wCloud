package com.medical.imaging.service;

import com.medical.imaging.dto.auth.*;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    void register(RegisterRequest registerRequest);
    void resetPassword(ResetPasswordRequest request);
    void changePassword(String token, ChangePasswordRequest request);
    void logout(String token);
    UserProfileResponse getUserProfile(String token);
    void updateUserProfile(String token, UpdateProfileRequest request);
    TokenResponse refreshToken(RefreshTokenRequest request);
} 