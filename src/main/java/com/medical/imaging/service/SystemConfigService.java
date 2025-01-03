package com.medical.imaging.service;

import com.medical.imaging.dto.config.SystemConfigDTO;
import com.medical.imaging.dto.config.SystemConfigRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface SystemConfigService {
    String getConfigValue(String key);
    
    void setConfigValue(String key, String value);
    
    boolean hasConfig(String key);
    
    void validateConfig(String key, String value);
    
    void initializeDefaultConfigs();
    
    SystemConfigDTO getConfig(String key);
    
    Page<SystemConfigDTO> getAllConfigs(Pageable pageable);
    
    void updateConfig(String key, SystemConfigRequest request);
    
    void deleteConfig(String key);
    
    Map<String, String> getConfigsByCategory(String category);
} 