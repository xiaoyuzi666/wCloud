package com.medical.imaging.service;

import com.medical.imaging.entity.Patient;
import com.medical.imaging.entity.Study;
import java.util.List;

public interface PatientService {
    Patient getPatient(Long patientId);
    List<Study> getPatientStudies(Long patientId);
} 