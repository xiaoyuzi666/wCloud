package com.medical.imaging.controller;

import com.medical.imaging.dto.config.*;
import com.medical.imaging.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
@Tag(name = "System Configuration", description = "APIs for managing system configurations")
@SecurityRequirement(name = "JWT")
public class SystemConfigController {

    private final SystemConfigService configService;

    @Operation(
        summary = "Get configuration value",
        description = "Retrieve the value of a specific configuration key"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Configuration value retrieved successfully",
        content = @Content(schema = @Schema(implementation = String.class))
    )
    @GetMapping("/{key}")
    public ResponseEntity<String> getConfigValue(
        @Parameter(description = "Configuration key")
        @PathVariable String key
    ) {
        return ResponseEntity.ok(configService.getConfigValue(key));
    }

    @Operation(
        summary = "Set configuration value",
        description = "Set or update a configuration value"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{key}")
    public ResponseEntity<Void> setConfigValue(
        @Parameter(description = "Configuration key")
        @PathVariable String key,
        @Parameter(description = "Configuration value")
        @RequestBody String value
    ) {
        configService.setConfigValue(key, value);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Initialize default configurations",
        description = "Initialize the system with default configuration values"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeDefaultConfigs() {
        configService.initializeDefaultConfigs();
        return ResponseEntity.ok().build();
    }
} 