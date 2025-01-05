package com.medical.imaging.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface DicomService {
    Map<String, Object> processDicomFile(MultipartFile file) throws IOException;
    Map<String, Object> getStudyDetails(String studyId) throws Exception;
    void processMultipleDicomFiles(List<MultipartFile> files, String uploadId, 
        Function<String, SseEmitter> emitterSupplier) throws Exception;
} 