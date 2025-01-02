package com.medical.imaging.repository;

import com.medical.imaging.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findBySeriesId(String seriesId);
    List<Image> findByStudyId(String studyId);
    
    @Query("SELECT i FROM Image i WHERE i.seriesId = :seriesId ORDER BY i.instanceNumber")
    List<Image> findBySeriesIdOrdered(String seriesId);
    
    boolean existsByInstanceId(String instanceId);
    
    @Query("SELECT COUNT(i) FROM Image i WHERE i.studyId = :studyId")
    long countByStudyId(String studyId);
} 