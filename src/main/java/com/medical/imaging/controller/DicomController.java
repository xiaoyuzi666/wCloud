package com.medical.imaging.controller;

import com.medical.imaging.service.DicomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/dicom")
@RequiredArgsConstructor
public class DicomController {
    
    private final DicomService dicomService;
    private final ConcurrentHashMap<String, SseEmitter> uploadEmitters = new ConcurrentHashMap<>();
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDicomFile(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = dicomService.processDicomFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/study/{studyId}")
    public ResponseEntity<?> getStudyDetails(@PathVariable String studyId) {
        try {
            Map<String, Object> studyDetails = dicomService.getStudyDetails(studyId);
            return ResponseEntity.ok(studyDetails);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/upload/batch")
    public ResponseEntity<?> uploadMultipleDicomFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("uploadId") String uploadId) {
        try {
            dicomService.processMultipleDicomFiles(files, uploadId, this::getEmitter);
            return ResponseEntity.ok(Map.of("message", "Upload started", "uploadId", uploadId));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/upload/progress/{uploadId}")
    public SseEmitter trackProgress(@PathVariable String uploadId) {
        SseEmitter emitter = new SseEmitter(300000L); // 5 minutes timeout
        uploadEmitters.put(uploadId, emitter);
        
        emitter.onCompletion(() -> uploadEmitters.remove(uploadId));
        emitter.onTimeout(() -> uploadEmitters.remove(uploadId));
        
        return emitter;
    }

    private SseEmitter getEmitter(String uploadId) {
        return uploadEmitters.get(uploadId);
    }
} 