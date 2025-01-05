package com.medical.imaging.controller;

import com.medical.imaging.dto.report.*;
import com.medical.imaging.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "报告管理", description = "检查报告的管理操作")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "生成报告", description = "为指定检查生成报告")
    @PostMapping("/studies/{studyId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ReportDTO> generateReport(
            @PathVariable Long studyId,
            @Valid @RequestBody ReportGenerateRequest request) {
        return ResponseEntity.ok(reportService.createReport(studyId, request));
    }

    @Operation(summary = "获取报告", description = "获取指定报告的详细信息")
    @GetMapping("/{reportId}")
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<ReportDTO> getReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReport(reportId));
    }

    @Operation(summary = "更新报告", description = "更新现有报告")
    @PutMapping("/{reportId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ReportDTO> updateReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ReportUpdateRequest request) {
        return ResponseEntity.ok(reportService.updateReport(reportId, request));
    }

    @Operation(summary = "删除报告", description = "删除指定报告")
    @DeleteMapping("/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取检查报告列表", description = "获取指定检查的所有报告")
    @GetMapping("/studies/{studyId}")
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<ReportDTO>> getStudyReports(
            @PathVariable Long studyId,
            @Parameter(description = "分页参数") Pageable pageable) {
        return ResponseEntity.ok(reportService.getStudyReports(studyId, pageable));
    }

    @Operation(summary = "创建报告模板", description = "创建新的报告模板")
    @PostMapping("/templates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportTemplateDTO> createTemplate(
            @Valid @RequestBody ReportTemplateRequest request) {
        return ResponseEntity.ok(reportService.createTemplate(request));
    }

    @Operation(summary = "更新报告模板", description = "更新现有报告模板")
    @PutMapping("/templates/{templateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportTemplateDTO> updateTemplate(
            @PathVariable Long templateId,
            @Valid @RequestBody ReportTemplateRequest request) {
        return ResponseEntity.ok(reportService.updateTemplate(templateId, request));
    }

    @Operation(summary = "删除报告模板", description = "删除指定报告模板")
    @DeleteMapping("/templates/{templateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long templateId) {
        reportService.deleteTemplate(templateId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取报告模板", description = "获取指定报告模板的详细信息")
    @GetMapping("/templates/{templateId}")
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<ReportTemplateDTO> getTemplate(@PathVariable Long templateId) {
        return ResponseEntity.ok(reportService.getTemplate(templateId));
    }

    @Operation(summary = "获取所有报告模板", description = "获取所有可用的报告模板")
    @GetMapping("/templates")
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<ReportTemplateDTO>> getAllTemplates(
            @Parameter(description = "分页参数") Pageable pageable) {
        return ResponseEntity.ok(reportService.getAllTemplates(pageable));
    }
} 