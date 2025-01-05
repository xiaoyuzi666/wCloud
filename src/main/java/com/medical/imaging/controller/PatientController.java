package com.medical.imaging.controller;

import com.medical.imaging.dto.patient.*;
import com.medical.imaging.service.PatientService;
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

@Tag(name = "患者管理", description = "患者信息的CRUD操作")
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "获取患者列表", description = "支持分页和条件查询")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<PatientDTO>> getAllPatients(
            @Parameter(description = "患者姓名（模糊查询）") 
            @RequestParam(required = false) String name,
            @Parameter(description = "患者ID（模糊查询）") 
            @RequestParam(required = false) String patientId,
            @Parameter(description = "分页参数") 
            Pageable pageable) {
        return ResponseEntity.ok(patientService.findPatients(name, patientId, pageable));
    }

    @Operation(summary = "获取单个患者", description = "根据ID获取患者详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PatientDTO> getPatient(
            @Parameter(description = "患者ID") 
            @PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @Operation(summary = "创建患者", description = "创建新的患者记录")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientDTO> createPatient(
            @Valid @RequestBody PatientCreateRequest request) {
        return ResponseEntity.ok(patientService.createPatient(request));
    }

    @Operation(summary = "更新患者", description = "更新现有患者信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientUpdateRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @Operation(summary = "删除患者", description = "删除患者记录")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取患者检查", description = "获取患者的所有检查记录")
    @GetMapping("/{id}/studies")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<StudyDTO>> getPatientStudies(
            @PathVariable Long id,
            Pageable pageable) {
        return ResponseEntity.ok(patientService.getPatientStudies(id, pageable));
    }
} 