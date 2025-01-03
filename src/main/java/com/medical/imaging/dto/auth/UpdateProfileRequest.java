package com.medical.imaging.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.Email;

@Data
public class UpdateProfileRequest {
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    private String department;
} 