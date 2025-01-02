package com.medical.imaging.service;

import com.medical.imaging.model.Patient;
import com.medical.imaging.model.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PatientService {
    Page<Patient> findPatients(String name, String patientId, Pageable pageable);
    Patient getPatient(Long id);
    Patient createPatient(Patient patient);
    Patient updatePatient(Long id, Patient patient);
    void deletePatient(Long id);
    List<Study> getPatientStudies(Long patientId);
} 