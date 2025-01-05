package com.medical.imaging.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequest {
    @Email(message = "Invalid email format")
    private String email;
    private String name;
    private String phone;
    private String department;
    private Set<String> roles;
} 