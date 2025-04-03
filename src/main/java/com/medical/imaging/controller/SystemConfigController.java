package com.medical.imaging.controller;

import com.medical.imaging.dto.config.*;
import com.medical.imaging.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "系统配置", description = "系统配置管理")
@RestController
@RequestMapping("/api/system/config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @Operation(summary = "获取配置值", description = "根据配置键获取配置值")
    @GetMapping("/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getConfig(
            @Parameter(description = "配置键") 
            @PathVariable String key) {
        return ResponseEntity.ok(systemConfigService.getConfig(key));
    }

    @Operation(summary = "设置配置值", description = "设置指定键的配置值")
    @PutMapping("/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> setConfig(
            @Parameter(description = "配置键") 
            @PathVariable String key,
            @Valid @RequestBody ConfigUpdateRequest request) {
        systemConfigService.setConfig(key, request.getValue());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取所有配置", description = "获取所有系统配置")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getAllConfigs() {
        return ResponseEntity.ok(systemConfigService.getAllConfigs());
    }

    @Operation(summary = "初始化默认配置", description = "初始化系统默认配置")
    @PostMapping("/init")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> initDefaultConfigs() {
        systemConfigService.initDefaultConfigs();
        return ResponseEntity.ok().build();
    }
} 