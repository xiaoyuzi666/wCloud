package com.medical.imaging.service;

import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
    void sendTemplateEmail(String to, String subject, String template, Map<String, Object> model);
} 