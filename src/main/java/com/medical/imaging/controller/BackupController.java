package com.medical.imaging.controller;

import com.medical.imaging.dto.backup.*;
import com.medical.imaging.service.BackupService;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/backup")
@RequiredArgsConstructor
@Tag(name = "System Backup", description = "APIs for system backup and restore operations")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ADMIN')")
public class BackupController {

    private final BackupService backupService;

    @Operation(
        summary = "Create backup",
        description = "Create a new system backup"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Backup created successfully",
        content = @Content(schema = @Schema(implementation = BackupResult.class))
    )
    @PostMapping
    public ResponseEntity<BackupResult> createBackup(
        @Parameter(description = "Backup configuration")
        @Valid @RequestBody BackupRequest request
    ) {
        return ResponseEntity.ok(backupService.createBackup(request));
    }

    @Operation(
        summary = "Restore from backup",
        description = "Restore system from a backup file"
    )
    @PostMapping("/restore/{backupId}")
    public ResponseEntity<RestoreResult> restore(
        @Parameter(description = "ID of the backup to restore from")
        @PathVariable String backupId
    ) {
        return ResponseEntity.ok(backupService.restore(backupId));
    }

    @Operation(
        summary = "Get backup status",
        description = "Get the status of a backup operation"
    )
    @GetMapping("/{backupId}/status")
    public ResponseEntity<BackupStatus> getBackupStatus(
        @Parameter(description = "ID of the backup")
        @PathVariable String backupId
    ) {
        return ResponseEntity.ok(backupService.getBackupStatus(backupId));
    }
} 