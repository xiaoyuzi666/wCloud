package com.medical.imaging.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "orthanc")
public class OrthancConfig {
    private String serverUrl;
    private String username;
    private String password;
    private int connectionTimeout;
    private int readTimeout;
} 