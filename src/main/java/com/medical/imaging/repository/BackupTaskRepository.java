package com.medical.imaging.repository;

import com.medical.imaging.entity.BackupTask;
import com.medical.imaging.enums.BackupStatus;
import com.medical.imaging.enums.BackupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Repository
public interface BackupTaskRepository extends JpaRepository<BackupTask, String> {
    
    @Query("SELECT COUNT(b) FROM BackupTask b WHERE b.status = :status")
    long countByStatus(BackupStatus status);

    @Query("SELECT AVG(TIMESTAMPDIFF(SECOND, b.startTime, b.endTime)) " +
           "FROM BackupTask b WHERE b.status = 'COMPLETED'")
    Duration calculateAverageBackupTime();

    @Query("SELECT MAX(b.endTime) FROM BackupTask b WHERE b.status = 'COMPLETED'")
    LocalDateTime findLastSuccessfulBackupTime();

    @Query("SELECT SUM(b.totalSize) FROM BackupTask b WHERE b.status = 'COMPLETED'")
    Long calculateTotalStorageUsed();

    @Query("SELECT b.type as type, COUNT(b) as count FROM BackupTask b " +
           "GROUP BY b.type")
    Map<BackupType, Long> countByBackupType();

    @Query("SELECT FUNCTION('DATE_FORMAT', b.startTime, '%Y-%m') as month, " +
           "SUM(b.totalSize) as size FROM BackupTask b " +
           "WHERE b.status = 'COMPLETED' " +
           "GROUP BY FUNCTION('DATE_FORMAT', b.startTime, '%Y-%m')")
    Map<String, Long> calculateStorageByMonth();
} 