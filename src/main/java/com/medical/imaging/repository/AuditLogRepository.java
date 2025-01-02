package com.medical.imaging.repository;

import com.medical.imaging.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {
    // 基础的 CRUD 操作由 JpaRepository 提供
    // 复杂查询功能由 JpaSpecificationExecutor 提供
} 