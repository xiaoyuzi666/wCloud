package com.medical.imaging.repository;

import com.medical.imaging.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByStudyId(Long studyId, Pageable pageable);
    boolean existsByStudyId(Long studyId);
} 