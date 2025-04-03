package com.medical.imaging.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    private String department;
} 