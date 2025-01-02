package com.medical.imaging.repository;

import com.medical.imaging.entity.ReportTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long> {
    Page<ReportTemplate> findByCategory(String category, Pageable pageable);
    Optional<ReportTemplate> findByIsDefaultTrue();
    boolean existsByNameAndIdNot(String name, Long id);
} 