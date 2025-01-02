package com.medical.imaging.controller;

import com.medical.imaging.model.Patient;
import com.medical.imaging.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "患者管理", description = "患者信息的CRUD操作")
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "获取患者列表", description = "支持分页和条件查询")
    @GetMapping
    public ResponseEntity<Page<Patient>> getAllPatients(
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
    public ResponseEntity<Patient> getPatient(
            @Parameter(description = "患者ID") 
            @PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.createPatient(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id, 
            @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/studies")
    public ResponseEntity<?> getPatientStudies(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientStudies(id));
    }
} 