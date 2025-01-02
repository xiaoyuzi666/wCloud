package com.medical.imaging.dto.user;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserCreateRequest {
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    @Email
    private String email;
    
    private String phone;
    private String department;
    private Set<String> roles;
} 