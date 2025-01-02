package com.medical.imaging.controller;

import com.medical.imaging.dto.*;
import com.medical.imaging.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangePasswordRequest request) {
        authService.changePassword(token, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.getUserProfile(token));
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateProfileRequest request) {
        authService.updateUserProfile(token, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
} 