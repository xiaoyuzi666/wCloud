package com.medical.imaging.repository;

import com.medical.imaging.model.Study;
import com.medical.imaging.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, JpaSpecificationExecutor<Study> {
    
    List<Study> findByPatient(Patient patient);
    
    List<Study> findByPatientOrderByStudyDateDesc(Patient patient);
    
    long countByStudyDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT CAST(DATE(s.studyDate) as string), COUNT(s) " +
           "FROM Study s " +
           "WHERE s.studyDate BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(s.studyDate)")
    List<Object[]> getDailyStudyCount(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p.name, COUNT(s) " +
           "FROM Study s " +
           "JOIN s.patient p " +
           "WHERE s.studyDate BETWEEN :startDate AND :endDate " +
           "GROUP BY p.name")
    List<Object[]> getStudyCountByPatient(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
} 