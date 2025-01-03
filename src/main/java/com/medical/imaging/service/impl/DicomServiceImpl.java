package com.medical.imaging.service.impl;

import com.medical.imaging.config.OrthancConfig;
import com.medical.imaging.entity.Patient;
import com.medical.imaging.entity.Study;
import com.medical.imaging.repository.PatientRepository;
import com.medical.imaging.repository.StudyRepository;
import com.medical.imaging.service.DicomService;
import com.medical.imaging.util.DicomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DicomServiceImpl implements DicomService {

    private final OrthancConfig orthancConfig;
    private final PatientRepository patientRepository;
    private final StudyRepository studyRepository;

    @Override
    @Transactional
    public void processDicomFile(MultipartFile file) throws IOException {
        Attributes attrs = DicomUtils.readDicomAttributes(file.getInputStream());
        
        // 处理患者信息
        Patient patient = processPatientInfo(attrs);
        
        // 处理检查信息
        Study study = processStudyInfo(attrs, patient);
        
        // 保存到数据库
        patientRepository.save(patient);
        studyRepository.save(study);
        
        // 上传到Orthanc服务器
        uploadToOrthanc(file);
    }

    private Patient processPatientInfo(Attributes attrs) {
        String patientId = attrs.getString(Tag.PatientID);
        return patientRepository.findByPatientId(patientId)
            .orElseGet(() -> {
                Patient newPatient = new Patient();
                newPatient.setPatientId(patientId);
                newPatient.setName(attrs.getString(Tag.PatientName));
                newPatient.setBirthDate(DicomUtils.parseDateTime(
                    attrs.getString(Tag.PatientBirthDate), "000000").toLocalDate());
                newPatient.setSex(attrs.getString(Tag.PatientSex));
                return newPatient;
            });
    }

    private Study processStudyInfo(Attributes attrs, Patient patient) {
        String studyInstanceUid = attrs.getString(Tag.StudyInstanceUID);
        return studyRepository.findByStudyInstanceUid(studyInstanceUid)
            .orElseGet(() -> {
                Study newStudy = new Study();
                newStudy.setPatient(patient);
                newStudy.setStudyInstanceUid(studyInstanceUid);
                newStudy.setAccessionNumber(attrs.getString(Tag.AccessionNumber));
                newStudy.setStudyDate(DicomUtils.getStudyDateTime(attrs));
                newStudy.setModality(attrs.getString(Tag.Modality));
                newStudy.setReferringPhysician(attrs.getString(Tag.ReferringPhysicianName));
                newStudy.setStudyDescription(attrs.getString(Tag.StudyDescription));
                return newStudy;
            });
    }

    private void uploadToOrthanc(MultipartFile file) {
        // TODO: 实现上传到Orthanc服务器的逻辑
        log.info("Uploading DICOM file to Orthanc server: {}", orthancConfig.getUrl());
    }
} 