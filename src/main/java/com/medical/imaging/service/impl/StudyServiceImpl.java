package com.medical.imaging.service.impl;

import com.medical.imaging.model.Patient;
import com.medical.imaging.model.Study;
import com.medical.imaging.repository.StudyRepository;
import com.medical.imaging.service.StudyService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final StudyRepository studyRepository;

    @Override
    public Page<Study> searchStudies(
            String patientName, 
            String patientId,
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Pageable pageable) {
        
        Specification<Study> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (patientName != null && !patientName.isEmpty()) {
                Join<Study, Patient> patientJoin = root.join("patient");
                predicates.add(cb.like(cb.lower(patientJoin.get("name")), 
                    "%" + patientName.toLowerCase() + "%"));
            }
            
            if (patientId != null && !patientId.isEmpty()) {
                Join<Study, Patient> patientJoin = root.join("patient");
                predicates.add(cb.like(patientJoin.get("patientId"), 
                    "%" + patientId + "%"));
            }
            
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("studyDate"), startDate));
            }
            
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("studyDate"), endDate));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return studyRepository.findAll(spec, pageable);
    }

    @Override
    public Study getStudy(Long id) {
        return studyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Study not found"));
    }

    @Override
    @Transactional
    public void deleteStudy(Long id) {
        studyRepository.deleteById(id);
    }

    @Override
    public List<?> getStudySeries(Long studyId) {
        Study study = getStudy(studyId);
        // 调用Orthanc API获取序列信息
        // TODO: 实现与Orthanc的集成
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取研究总数
        long totalStudies = studyRepository.countByStudyDateBetween(startDate, endDate);
        stats.put("totalStudies", totalStudies);
        
        // 获取每天的研究数量
        List<Object[]> dailyStats = studyRepository.getDailyStudyCount(startDate, endDate);
        stats.put("dailyStats", dailyStats);
        
        // 获取每个患者的研究数量
        List<Object[]> patientStats = studyRepository.getStudyCountByPatient(startDate, endDate);
        stats.put("patientStats", patientStats);
        
        return stats;
    }
} 