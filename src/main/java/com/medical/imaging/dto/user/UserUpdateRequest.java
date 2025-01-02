package com.medical.imaging.dto.user;

import lombok.Data;
import javax.validation.constraints.Email;

@Data
public class UserUpdateRequest {
    @Email
    private String email;
    private String phone;
    private String department;
} 