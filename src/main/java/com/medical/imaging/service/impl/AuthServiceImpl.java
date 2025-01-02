package com.medical.imaging.service.impl;

import com.medical.imaging.dto.*;
import com.medical.imaging.model.User;
import com.medical.imaging.service.AuthService;
import com.medical.imaging.security.JwtUtil;
import com.medical.imaging.repository.UserRepository;
import com.medical.imaging.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new LoginResponse(token, refreshToken);
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        // 检查用户是否已存在
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        userRepository.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        // 验证验证码
        if (!verifyCode(request.getEmail(), request.getVerificationCode())) {
            throw new RuntimeException("Invalid verification code");
        }

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void changePassword(String token, ChangePasswordRequest request) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 验证旧密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        // 验证新密码确认
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void logout(String token) {
        // 将token加入黑名单
        jwtUtil.invalidateToken(token.substring(7));
    }

    @Override
    public UserProfileResponse getUserProfile(String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileResponse(
            user.getUsername(),
            user.getEmail(),
            user.getPhone(),
            user.getDepartment()
        );
    }

    @Override
    public void updateUserProfile(String token, UpdateProfileRequest request) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        userRepository.save(user);
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtUtil.extractUsernameFromRefreshToken(request.getRefreshToken());
        UserDetails userDetails = userService.loadUserByUsername(username);
        
        String newToken = jwtUtil.generateToken(userDetails);
        return new TokenResponse(newToken);
    }

    private boolean verifyCode(String email, String code) {
        // 实现验证码验证逻辑
        return true; // 临时返回
    }
} 