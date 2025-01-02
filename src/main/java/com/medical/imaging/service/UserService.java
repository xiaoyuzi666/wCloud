package com.medical.imaging.service;

import com.medical.imaging.dto.user.UserCreateRequest;
import com.medical.imaging.dto.user.UserDTO;
import com.medical.imaging.dto.user.UserUpdateRequest;
import com.medical.imaging.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserCreateRequest request);
    UserDTO updateUser(Long userId, UserUpdateRequest request);
    void deleteUser(Long userId);
    UserDTO getUserById(Long userId);
    void changePassword(Long userId, String oldPassword, String newPassword);
}