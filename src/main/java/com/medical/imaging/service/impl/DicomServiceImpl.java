package com.medical.imaging.service.impl;

import com.medical.imaging.config.OrthancConfig;
import com.medical.imaging.model.Patient;
import com.medical.imaging.model.Study;
import com.medical.imaging.repository.PatientRepository;
import com.medical.imaging.repository.StudyRepository;
import com.medical.imaging.service.DicomService;
import lombok.RequiredArgsConstructor;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DicomServiceImpl implements DicomService {

    private final OrthancConfig orthancConfig;
    private final PatientRepository patientRepository;
    private final StudyRepository studyRepository;
    private final RestTemplate restTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public Map<String, Object> processDicomFile(MultipartFile file) throws Exception {
        // 1. 读取DICOM文件元数据
        DicomInputStream dis = new DicomInputStream(file.getInputStream());
        Attributes attrs = dis.readDataset();
        
        // 2. 提取患者信息
        String patientId = attrs.getString(org.dcm4che3.data.Tag.PatientID);
        String patientName = attrs.getString(org.dcm4che3.data.Tag.PatientName);
        String patientBirthDate = attrs.getString(org.dcm4che3.data.Tag.PatientBirthDate);
        String patientSex = attrs.getString(org.dcm4che3.data.Tag.PatientSex);
        
        // 3. 保存患者信息到数据库
        Patient patient = patientRepository.findByPatientId(patientId)
            .orElseGet(() -> {
                Patient newPatient = new Patient();
                newPatient.setPatientId(patientId);
                newPatient.setName(patientName);
                newPatient.setGender(patientSex);
                if (patientBirthDate != null) {
                    newPatient.setBirthDate(LocalDate.parse(patientBirthDate));
                }
                return patientRepository.save(newPatient);
            });
        
        // 4. 上传到Orthanc
        String orthancResponse = uploadToOrthanc(file);
        
        // 5. 保存研究信息
        Study study = new Study();
        study.setPatient(patient);
        study.setStudyInstanceUid(attrs.getString(org.dcm4che3.data.Tag.StudyInstanceUID));
        study.setStudyDate(LocalDateTime.now());
        study.setStudyDescription(attrs.getString(org.dcm4che3.data.Tag.StudyDescription));
        study.setOrthancStudyId(orthancResponse);
        studyRepository.save(study);
        
        // 6. 返回处理结果
        Map<String, Object> result = new HashMap<>();
        result.put("patientId", patient.getId());
        result.put("studyId", study.getId());
        result.put("orthancId", orthancResponse);
        
        return result;
    }

    @Override
    public Map<String, Object> getStudyDetails(String studyId) throws Exception {
        // 从Orthanc获取研究详情
        String url = orthancConfig.getServerUrl() + "/studies/" + studyId;
        
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Map.class
        );
        
        return response.getBody();
    }
    
    private String uploadToOrthanc(MultipartFile file) throws Exception {
        String url = orthancConfig.getServerUrl() + "/instances";
        
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            Map.class
        );
        
        if (response.getBody() != null && response.getBody().containsKey("ID")) {
            return (String) response.getBody().get("ID");
        }
        
        throw new RuntimeException("Failed to upload to Orthanc");
    }
    
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(orthancConfig.getUsername(), orthancConfig.getPassword());
        return headers;
    }

    @Override
    public void processMultipleDicomFiles(
            List<MultipartFile> files, 
            String uploadId,
            Function<String, SseEmitter> emitterSupplier) throws Exception {
        
        executorService.submit(() -> {
            try {
                int total = files.size();
                int processed = 0;
                List<Map<String, Object>> results = new ArrayList<>();
                
                for (MultipartFile file : files) {
                    try {
                        Map<String, Object> result = processDicomFile(file);
                        results.add(result);
                        processed++;
                        
                        // 发送进度更新
                        sendProgressUpdate(emitterSupplier.apply(uploadId), processed, total, results);
                    } catch (Exception e) {
                        // 记录错误但继续处理其他文件
                        sendErrorUpdate(emitterSupplier.apply(uploadId), 
                            file.getOriginalFilename(), e.getMessage());
                    }
                }
                
                // 发送完成通知
                sendCompleteUpdate(emitterSupplier.apply(uploadId), results);
                
            } catch (Exception e) {
                // 发送致命错误通知
                sendFatalErrorUpdate(emitterSupplier.apply(uploadId), e.getMessage());
            }
        });
    }

    @Override
    public List<Map<String, Object>> searchStudies(
            String patientName, 
            String patientId, 
            String studyDate) throws Exception {
        
        String url = orthancConfig.getServerUrl() + "/tools/find";
        
        Map<String, Object> query = new HashMap<>();
        if (patientName != null) {
            query.put("PatientName", patientName);
        }
        if (patientId != null) {
            query.put("PatientID", patientId);
        }
        if (studyDate != null) {
            query.put("StudyDate", studyDate);
        }
        
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(query, headers);
        
        ResponseEntity<List> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            List.class
        );
        
        return response.getBody();
    }

    private void sendProgressUpdate(SseEmitter emitter, int processed, int total, 
                                  List<Map<String, Object>> results) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("type", "progress");
            data.put("processed", processed);
            data.put("total", total);
            data.put("percentage", (processed * 100.0) / total);
            data.put("results", results);
            
            emitter.send(SseEmitter.event().data(data));
        } catch (Exception e) {
            // 忽略已关闭的emitter
        }
    }

    private void sendErrorUpdate(SseEmitter emitter, String filename, String error) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("type", "error");
            data.put("filename", filename);
            data.put("error", error);
            
            emitter.send(SseEmitter.event().data(data));
        } catch (Exception e) {
            // 忽略已关闭的emitter
        }
    }

    private void sendCompleteUpdate(SseEmitter emitter, List<Map<String, Object>> results) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("type", "complete");
            data.put("results", results);
            
            emitter.send(SseEmitter.event().data(data));
            emitter.complete();
        } catch (Exception e) {
            // 忽略已关闭的emitter
        }
    }

    private void sendFatalErrorUpdate(SseEmitter emitter, String error) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("type", "fatalError");
            data.put("error", error);
            
            emitter.send(SseEmitter.event().data(data));
            emitter.complete();
        } catch (Exception e) {
            // 忽略已关闭的emitter
        }
    }
} 