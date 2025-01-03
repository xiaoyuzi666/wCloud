package com.medical.imaging.controller;

import com.medical.imaging.dto.report.*;
import com.medical.imaging.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Report Management", description = "APIs for managing medical reports")
public class ReportController {

    private final ReportService reportService;

    @Operation(
        summary = "Generate new report",
        description = "Generate a new report based on study and template"
    )
    @PostMapping
    public ResponseEntity<ReportDTO> generateReport(
        @Valid @RequestBody ReportGenerateRequest request
    ) {
        return ResponseEntity.ok(reportService.generateReport(request));
    }

    @Operation(
        summary = "Get report details",
        description = "Retrieve details of a specific report"
    )
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDTO> getReport(
        @Parameter(description = "ID of the report")
        @PathVariable Long reportId
    ) {
        return ResponseEntity.ok(reportService.getReport(reportId));
    }

    @Operation(
        summary = "Update report",
        description = "Update an existing report"
    )
    @PutMapping("/{reportId}")
    public ResponseEntity<ReportDTO> updateReport(
        @Parameter(description = "ID of the report")
        @PathVariable Long reportId,
        @Valid @RequestBody ReportUpdateRequest request
    ) {
        return ResponseEntity.ok(reportService.updateReport(reportId, request));
    }

    @Operation(
        summary = "Get reports by study",
        description = "Retrieve all reports associated with a study"
    )
    @GetMapping("/study/{studyId}")
    public ResponseEntity<Page<ReportDTO>> getReportsByStudy(
        @Parameter(description = "ID of the study")
        @PathVariable Long studyId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(reportService.getReportsByStudy(studyId, pageable));
    }
} 