package com.medical.imaging.service;

import com.medical.imaging.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface AuditLogService {
    void logAction(String username, String action, String resourceType, 
                  String resourceId, String details, String ipAddress);
    Page<AuditLog> searchLogs(String username, String action, String resourceType,
                             LocalDateTime startDate, LocalDateTime endDate, 
                             Pageable pageable);
} 