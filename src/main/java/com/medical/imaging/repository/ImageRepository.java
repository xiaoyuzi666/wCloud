package com.medical.imaging.repository;

import com.medical.imaging.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findBySeriesIdOrderByInstanceNumber(String seriesId);
    
    @Query("SELECT i FROM Image i WHERE i.seriesId = ?1 ORDER BY i.instanceNumber")
    List<Image> findBySeriesIdOrdered(String seriesId);
} 