package com.medical.imaging.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
    void sendEmailWithAttachment(String to, String subject, String content, String attachmentPath);
} 