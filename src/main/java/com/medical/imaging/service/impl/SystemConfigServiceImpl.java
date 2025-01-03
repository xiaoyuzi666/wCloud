package com.medical.imaging.service.impl;

import com.medical.imaging.entity.SystemConfig;
import com.medical.imaging.exception.BusinessException;
import com.medical.imaging.repository.SystemConfigRepository;
import com.medical.imaging.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository configRepository;

    @Override
    public String getConfig(String key) {
        return configRepository.findById(key)
            .map(SystemConfig::getValue)
            .orElseThrow(() -> new BusinessException("Config not found: " + key));
    }

    @Override
    @Transactional
    public void setConfig(String key, String value) {
        validateConfig(key, value);
        
        SystemConfig config = configRepository.findById(key)
            .orElseGet(() -> {
                SystemConfig newConfig = new SystemConfig();
                newConfig.setKey(key);
                return newConfig;
            });
        
        config.setValue(value);
        configRepository.save(config);
    }

    @Override
    public Map<String, String> getAllConfigs() {
        Map<String, String> configs = new HashMap<>();
        configRepository.findAll().forEach(config -> 
            configs.put(config.getKey(), config.getValue())
        );
        return configs;
    }

    @Override
    public void validateConfig(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            throw new BusinessException("Config key cannot be empty");
        }
        if (value == null) {
            throw new BusinessException("Config value cannot be null");
        }

        switch (key) {
            case "storage.path":
                validateStoragePath(value);
                break;
            case "dicom.aet":
                validateAETitle(value);
                break;
            case "email.enabled":
                validateBoolean(value);
                break;
            // 添加其他配置项的验证
        }
    }

    @Override
    @Transactional
    public void initDefaultConfigs() {
        setDefaultConfig("storage.path", "/data/medical-imaging");
        setDefaultConfig("dicom.aet", "MEDICAL_IMAGING");
        setDefaultConfig("dicom.port", "11112");
        setDefaultConfig("email.enabled", "false");
        setDefaultConfig("email.smtp.host", "smtp.example.com");
        setDefaultConfig("email.smtp.port", "587");
        setDefaultConfig("backup.enabled", "true");
        setDefaultConfig("backup.schedule", "0 0 2 * * ?");
        setDefaultConfig("compression.enabled", "true");
        setDefaultConfig("compression.quality", "0.8");
    }

    private void setDefaultConfig(String key, String value) {
        if (!configRepository.existsById(key)) {
            SystemConfig config = new SystemConfig();
            config.setKey(key);
            config.setValue(value);
            config.setDescription(getDefaultDescription(key));
            config.setCategory(getConfigCategory(key));
            configRepository.save(config);
        }
    }

    private void validateStoragePath(String path) {
        if (path.trim().isEmpty()) {
            throw new BusinessException("Storage path cannot be empty");
        }
        // 可以添加更多的路径验证逻辑
    }

    private void validateAETitle(String aet) {
        if (aet.length() > 16) {
            throw new BusinessException("AE Title cannot be longer than 16 characters");
        }
        if (!aet.matches("^[A-Za-z0-9_-]+$")) {
            throw new BusinessException("AE Title can only contain letters, numbers, underscore and hyphen");
        }
    }

    private void validateBoolean(String value) {
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new BusinessException("Value must be 'true' or 'false'");
        }
    }

    private String getDefaultDescription(String key) {
        switch (key) {
            case "storage.path":
                return "Base storage path for medical images";
            case "dicom.aet":
                return "DICOM Application Entity Title";
            case "dicom.port":
                return "DICOM service port";
            case "email.enabled":
                return "Enable/disable email notifications";
            default:
                return "";
        }
    }

    private String getConfigCategory(String key) {
        if (key.startsWith("storage.")) {
            return "storage";
        } else if (key.startsWith("dicom.")) {
            return "dicom";
        } else if (key.startsWith("email.")) {
            return "email";
        } else if (key.startsWith("backup.")) {
            return "backup";
        } else if (key.startsWith("compression.")) {
            return "compression";
        } else {
            return "other";
        }
    }
} 