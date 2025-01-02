package com.medical.imaging.repository;

import com.medical.imaging.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, 
                                        JpaSpecificationExecutor<Patient> {
    Optional<Patient> findByPatientId(String patientId);
} 