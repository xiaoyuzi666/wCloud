package com.medical.imaging.service;

import com.medical.imaging.dto.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserCreateRequest request);
    UserDTO updateUser(Long userId, UserUpdateRequest request);
    void deleteUser(Long userId);
    UserDTO getUser(Long userId);
    Page<UserDTO> getAllUsers(Pageable pageable);
    void changePassword(Long userId, PasswordChangeRequest request);
    void resetPassword(String email);
    void enableUser(Long userId);
    void disableUser(Long userId);
}