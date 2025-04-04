package com.medical.imaging.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String department;
    private Set<String> roles;
    private LocalDateTime lastLoginTime;
    private Boolean enabled;
} 