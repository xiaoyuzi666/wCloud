package com.medical.imaging.repository;

import com.medical.imaging.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, JpaSpecificationExecutor<Study> {
    long countByStudyDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT DATE(s.studyDate) as date, COUNT(s) as count FROM Study s " +
           "WHERE s.studyDate BETWEEN ?1 AND ?2 GROUP BY DATE(s.studyDate)")
    List<Object[]> getDailyStudyCount(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p.name, COUNT(s) FROM Study s JOIN s.patient p " +
           "WHERE s.studyDate BETWEEN ?1 AND ?2 GROUP BY p.name")
    List<Object[]> getStudyCountByPatient(LocalDateTime startDate, LocalDateTime endDate);
} 