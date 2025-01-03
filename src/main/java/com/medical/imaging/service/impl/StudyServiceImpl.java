package com.medical.imaging.service.impl;

import com.medical.imaging.dto.study.*;
import com.medical.imaging.entity.Study;
import com.medical.imaging.repository.StudyRepository;
import com.medical.imaging.service.StudyService;
import com.medical.imaging.exception.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final StudyRepository studyRepository;

    @Override
    public Page<StudyDTO> searchStudies(StudySearchRequest request, Pageable pageable) {
        Specification<Study> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getPatientName() != null) {
                predicates.add(cb.like(
                    root.get("patient").get("name"),
                    "%" + request.getPatientName() + "%"
                ));
            }

            if (request.getPatientId() != null) {
                predicates.add(cb.equal(
                    root.get("patient").get("patientId"),
                    request.getPatientId()
                ));
            }

            if (request.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                    root.get("studyDate"),
                    request.getStartDate()
                ));
            }

            if (request.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                    root.get("studyDate"),
                    request.getEndDate()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return studyRepository.findAll(spec, pageable)
            .map(this::convertToDTO);
    }

    @Override
    public StudyDTO getStudy(Long id) {
        Study study = studyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Study not found"));
        return convertToDTO(study);
    }

    @Override
    @Transactional
    public void deleteStudy(Long id) {
        if (!studyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Study not found");
        }
        studyRepository.deleteById(id);
    }

    @Override
    public Page<SeriesDTO> getStudySeries(Long studyId, Pageable pageable) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new ResourceNotFoundException("Study not found"));
        // TODO: Implement series retrieval logic
        return Page.empty();
    }

    @Override
    public StudyStatisticsDTO getStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return StudyStatisticsDTO.builder()
            .totalStudies(studyRepository.countByStudyDateBetween(startDate, endDate))
            .studiesByDay(studyRepository.getDailyStudyCount(startDate, endDate))
            .studiesByPatient(studyRepository.getStudyCountByPatient(startDate, endDate))
            .build();
    }

    private StudyDTO convertToDTO(Study study) {
        return StudyDTO.builder()
            .id(study.getId())
            .patientId(study.getPatient().getPatientId())
            .patientName(study.getPatient().getName())
            .studyInstanceUid(study.getStudyInstanceUid())
            .accessionNumber(study.getAccessionNumber())
            .studyDate(study.getStudyDate())
            .description(study.getDescription())
            .modality(study.getModality())
            .status(study.getStatus())
            .referringPhysician(study.getReferringPhysician())
            .numberOfSeries(study.getNumberOfSeries())
            .numberOfInstances(study.getNumberOfInstances())
            .createdAt(study.getCreatedAt())
            .updatedAt(study.getUpdatedAt())
            .build();
    }
} 