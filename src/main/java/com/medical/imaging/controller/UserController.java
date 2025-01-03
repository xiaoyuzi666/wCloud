package com.medical.imaging.controller;

import com.medical.imaging.dto.user.*;
import com.medical.imaging.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing system users")
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "Create new user",
        description = "Create a new user in the system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Username already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Operation(
        summary = "Update user",
        description = "Update an existing user's information"
    )
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(
        @Parameter(description = "ID of the user to update")
        @PathVariable Long userId,
        @Valid @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @Operation(
        summary = "Change password",
        description = "Change user's password"
    )
    @PostMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(
        @Parameter(description = "ID of the user")
        @PathVariable Long userId,
        @Valid @RequestBody PasswordChangeRequest request
    ) {
        userService.changePassword(userId, request);
        return ResponseEntity.ok().build();
    }
} 