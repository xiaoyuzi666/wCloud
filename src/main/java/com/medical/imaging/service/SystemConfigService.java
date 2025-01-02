package com.medical.imaging.service;

import com.medical.imaging.entity.SystemConfig;
import com.medical.imaging.exception.BusinessException;

public interface SystemConfigService {
    String getConfigValue(String key);
    void setConfigValue(String key, String value);
    boolean hasConfig(String key);
    void validateConfig(String key, String value);
    void initializeDefaultConfigs();
} 