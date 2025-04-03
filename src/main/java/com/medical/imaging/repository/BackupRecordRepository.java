package com.medical.imaging.repository;

import com.medical.imaging.model.BackupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupRecordRepository extends JpaRepository<BackupRecord, Long>, JpaSpecificationExecutor<BackupRecord> {
    // 基础的 CRUD 操作由 JpaRepository 提供
    // 复杂查询功能由 JpaSpecificationExecutor 提供
} 