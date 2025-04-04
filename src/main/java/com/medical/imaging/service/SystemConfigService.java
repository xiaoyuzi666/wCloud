package com.medical.imaging.service;

import java.util.Map;

public interface SystemConfigService {
    String getConfig(String key);
    void setConfig(String key, String value);
    Map<String, String> getAllConfigs();
    void validateConfig(String key, String value);
    void initDefaultConfigs();
} 