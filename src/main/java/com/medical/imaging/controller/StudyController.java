package com.medical.imaging.controller;

import com.medical.imaging.model.Study;
import com.medical.imaging.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "检查管理", description = "影像检查的管理操作")
@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @Operation(summary = "搜索检查", description = "支持多条件搜索和分页")
    @GetMapping
    public ResponseEntity<Page<Study>> searchStudies(
            @Parameter(description = "患者姓名") 
            @RequestParam(required = false) String patientName,
            @Parameter(description = "患者ID") 
            @RequestParam(required = false) String patientId,
            @Parameter(description = "开始日期") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
                LocalDateTime startDate,
            @Parameter(description = "结束日期") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
                LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(studyService.searchStudies(patientName, patientId, startDate, endDate, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Study> getStudy(@PathVariable Long id) {
        return ResponseEntity.ok(studyService.getStudy(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudy(@PathVariable Long id) {
        studyService.deleteStudy(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/series")
    public ResponseEntity<?> getStudySeries(@PathVariable Long id) {
        return ResponseEntity.ok(studyService.getStudySeries(id));
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStudyStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
                LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
                LocalDateTime endDate) {
        return ResponseEntity.ok(studyService.getStatistics(startDate, endDate));
    }
} 