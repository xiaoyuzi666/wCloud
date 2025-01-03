package com.medical.imaging.repository;

import com.medical.imaging.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {
    Optional<SystemConfig> findByKey(String key);
    boolean existsByKey(String key);
    List<SystemConfig> findByCategory(String category);
} 