package com.medical.imaging.service.impl;

import com.medical.imaging.model.Patient;
import com.medical.imaging.model.Study;
import com.medical.imaging.repository.PatientRepository;
import com.medical.imaging.repository.StudyRepository;
import com.medical.imaging.service.PatientService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final StudyRepository studyRepository;

    @Override
    public Page<Patient> findPatients(String name, String patientId, Pageable pageable) {
        Specification<Patient> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), 
                    "%" + name.toLowerCase() + "%"));
            }
            
            if (patientId != null && !patientId.isEmpty()) {
                predicates.add(cb.like(root.get("patientId"), 
                    "%" + patientId + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return patientRepository.findAll(spec, pageable);
    }

    @Override
    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Override
    @Transactional
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public Patient updatePatient(Long id, Patient patient) {
        Patient existingPatient = getPatient(id);
        
        existingPatient.setName(patient.getName());
        existingPatient.setPatientId(patient.getPatientId());
        existingPatient.setBirthDate(patient.getBirthDate());
        existingPatient.setGender(patient.getGender());
        existingPatient.setPhoneNumber(patient.getPhoneNumber());
        
        return patientRepository.save(existingPatient);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public List<Study> getPatientStudies(Long patientId) {
        Patient patient = getPatient(patientId);
        return studyRepository.findByPatient(patient);
    }
} 