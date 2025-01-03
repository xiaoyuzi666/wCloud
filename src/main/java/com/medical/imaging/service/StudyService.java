package com.medical.imaging.service;

import com.medical.imaging.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StudyService {
    Page<Study> searchStudies(
        String patientName, 
        String patientId,
        LocalDateTime startDate, 
        LocalDateTime endDate, 
        Pageable pageable
    );
    
    Study getStudy(Long id);
    void deleteStudy(Long id);
    List<?> getStudySeries(Long studyId);
    Map<String, Object> getStatistics(LocalDateTime startDate, LocalDateTime endDate);
} 