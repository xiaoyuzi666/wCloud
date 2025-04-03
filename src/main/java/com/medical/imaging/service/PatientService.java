package com.medical.imaging.service;

import com.medical.imaging.dto.patient.*;
import com.medical.imaging.dto.study.StudyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    Page<PatientDTO> findPatients(String name, String patientId, Pageable pageable);
    PatientDTO getPatient(Long id);
    PatientDTO createPatient(PatientCreateRequest request);
    PatientDTO updatePatient(Long id, PatientUpdateRequest request);
    void deletePatient(Long id);
    Page<StudyDTO> getPatientStudies(Long id, Pageable pageable);
} 