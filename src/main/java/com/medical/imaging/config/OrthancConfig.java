package com.medical.imaging.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "orthanc")
public class OrthancConfig {
    private String url;
    private String username;
    private String password;
    private Integer maxConnections;
    private Integer connectionTimeout;
    private Integer readTimeout;
    private Boolean compression;
    private String storageDirectory;
} 