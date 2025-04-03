package com.medical.imaging.repository;

import com.medical.imaging.entity.BackupConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackupConfigRepository extends JpaRepository<BackupConfig, Long> {
    
    @Query("SELECT b FROM BackupConfig b ORDER BY b.id DESC LIMIT 1")
    Optional<BackupConfig> findCurrentConfig();
} 