package com.medical.imaging.repository;

import com.medical.imaging.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    
    Optional<SystemConfig> findByKey(String key);
    
    boolean existsByKey(String key);
    
    void deleteByKey(String key);
    
    @Query("SELECT s FROM SystemConfig s WHERE s.key LIKE :prefix%")
    List<SystemConfig> findByKeyStartingWith(String prefix);
    
    @Query("SELECT s FROM SystemConfig s WHERE s.updatedAt = " +
           "(SELECT MAX(sc.updatedAt) FROM SystemConfig sc WHERE sc.key = s.key)")
    List<SystemConfig> findLatestConfigs();
} 