package com.medical.imaging.service.impl;

import com.medical.imaging.dto.auth.*;
import com.medical.imaging.entity.User;
import com.medical.imaging.repository.UserRepository;
import com.medical.imaging.security.JwtUtil;
import com.medical.imaging.service.AuthService;
import com.medical.imaging.service.EmailService;
import com.medical.imaging.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new BusinessException("User not found"));

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(toUserDetails(user));
        String refreshToken = jwtUtil.generateRefreshToken(toUserDetails(user));

        return LoginResponse.builder()
            .token(token)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(3600L)
            .user(toUserProfileResponse(user))
            .build();
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setDepartment(request.getDepartment());
        user.setRoles(new HashSet<>(request.getRole() != null ? 
            Set.of(request.getRole()) : Set.of("ROLE_USER")));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException("User not found"));

        // 验证验证码
        if (!verifyResetCode(request.getEmail(), request.getVerificationCode())) {
            throw new BusinessException("Invalid verification code");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String token, ChangePasswordRequest request) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void logout(String token) {
        jwtUtil.invalidateToken(token.substring(7));
    }

    @Override
    public UserProfileResponse getUserProfile(String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("User not found"));

        return toUserProfileResponse(user);
    }

    @Override
    @Transactional
    public void updateUserProfile(String token, UpdateProfileRequest request) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("User not found"));

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }

        userRepository.save(user);
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtUtil.extractUsernameFromRefreshToken(request.getRefreshToken());
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("User not found"));

        String newToken = jwtUtil.generateToken(toUserDetails(user));
        
        return TokenResponse.builder()
            .token(newToken)
            .tokenType("Bearer")
            .expiresIn(3600L)
            .build();
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getRoles().toArray(new String[0]))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.getEnabled())
            .build();
    }

    private UserProfileResponse toUserProfileResponse(User user) {
        return UserProfileResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .name(user.getName())
            .department(user.getDepartment())
            .roles(user.getRoles())
            .lastLoginTime(user.getLastLoginTime())
            .enabled(user.getEnabled())
            .build();
    }

    private boolean verifyResetCode(String email, String code) {
        // TODO: 实现验证码验证逻辑
        return true;
    }
} 