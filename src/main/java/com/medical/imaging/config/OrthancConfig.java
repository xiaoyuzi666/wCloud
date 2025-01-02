package com.medical.imaging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "orthanc")
public class OrthancConfig {
    private String serverUrl;
    private String username;
    private String password;
} 