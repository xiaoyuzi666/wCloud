package com.medical.imaging.service;

import com.medical.imaging.dto.study.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface StudyService {
    Page<StudyDTO> searchStudies(StudySearchRequest request, Pageable pageable);
    StudyDTO getStudy(Long id);
    void deleteStudy(Long id);
    Page<SeriesDTO> getStudySeries(Long studyId, Pageable pageable);
    StudyStatisticsDTO getStatistics(LocalDateTime startDate, LocalDateTime endDate);
} 