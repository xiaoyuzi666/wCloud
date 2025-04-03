package com.medical.imaging.service.impl;

import com.medical.imaging.entity.Patient;
import com.medical.imaging.entity.Study;
import com.medical.imaging.repository.PatientRepository;
import com.medical.imaging.repository.StudyRepository;
import com.medical.imaging.service.PatientService;
import com.medical.imaging.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final StudyRepository studyRepository;

    @Override
    public Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    @Override
    public List<Study> getPatientStudies(Long patientId) {
        Patient patient = getPatient(patientId);
        return studyRepository.findByPatient(patient);
    }
} 