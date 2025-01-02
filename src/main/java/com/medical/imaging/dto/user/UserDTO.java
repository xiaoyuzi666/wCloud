package com.medical.imaging.dto.user;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String department;
    private Set<String> roles;
    private boolean enabled;
} 