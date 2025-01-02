package com.medical.imaging.service;

import com.medical.imaging.dto.study.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyService {
    StudyDTO createStudy(StudyCreateRequest request);
    StudyDTO updateStudy(Long studyId, StudyUpdateRequest request);
    void deleteStudy(Long studyId);
    StudyDTO getStudy(Long studyId);
    Page<StudyDTO> getAllStudies(StudySearchRequest request, Pageable pageable);
    StudyStatisticsDTO getStudyStatistics(StudyStatisticsRequest request);
    
    SeriesDTO addSeries(Long studyId, SeriesCreateRequest request);
    void deleteSeries(Long seriesId);
    Page<SeriesDTO> getStudySeries(Long studyId, Pageable pageable);
} 