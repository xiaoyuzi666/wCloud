package com.medical.imaging.service.impl;

import com.medical.imaging.entity.SystemConfig;
import com.medical.imaging.repository.SystemConfigRepository;
import com.medical.imaging.service.SystemConfigService;
import com.medical.imaging.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository configRepository;
    private final Map<String, String> defaultConfigs = new HashMap<>();

    @PostConstruct
    public void init() {
        defaultConfigs.put("backup.directory", "/data/backups");
        defaultConfigs.put("backup.retention.days", "30");
        defaultConfigs.put("backup.max.concurrent.tasks", "3");
        defaultConfigs.put("email.admin", "admin@example.com");
        defaultConfigs.put("db.name", "medical_imaging");
    }

    @Override
    public String getConfigValue(String key) {
        return configRepository.findByKey(key)
            .map(SystemConfig::getValue)
            .orElseThrow(() -> new BusinessException("Configuration not found: " + key));
    }

    @Override
    @Transactional
    public void setConfigValue(String key, String value) {
        validateConfig(key, value);
        SystemConfig config = configRepository.findByKey(key)
            .orElse(new SystemConfig(key));
        config.setValue(value);
        configRepository.save(config);
        log.info("System configuration updated - key: {}, value: {}", key, value);
    }

    @Override
    public boolean hasConfig(String key) {
        return configRepository.existsByKey(key);
    }

    @Override
    public void validateConfig(String key, String value) {
        // 实现配置验证逻辑
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException("Configuration value cannot be empty");
        }

        switch (key) {
            case "backup.retention.days":
                validateNumericConfig(value, 1, 365);
                break;
            case "backup.max.concurrent.tasks":
                validateNumericConfig(value, 1, 10);
                break;
            case "email.admin":
                validateEmailConfig(value);
                break;
            // 添加其他配置项的验证
        }
    }

    @Override
    @Transactional
    public void initializeDefaultConfigs() {
        defaultConfigs.forEach((key, value) -> {
            if (!hasConfig(key)) {
                setConfigValue(key, value);
                log.info("Initialized default configuration - key: {}, value: {}", key, value);
            }
        });
    }

    private void validateNumericConfig(String value, int min, int max) {
        try {
            int numValue = Integer.parseInt(value);
            if (numValue < min || numValue > max) {
                throw new BusinessException(
                    String.format("Value must be between %d and %d", min, max));
            }
        } catch (NumberFormatException e) {
            throw new BusinessException("Value must be a valid number");
        }
    }

    private void validateEmailConfig(String value) {
        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessException("Invalid email format");
        }
    }
} 