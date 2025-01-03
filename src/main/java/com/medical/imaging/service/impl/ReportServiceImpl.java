package com.medical.imaging.service.impl;

import com.medical.imaging.dto.report.*;
import com.medical.imaging.entity.Report;
import com.medical.imaging.entity.ReportTemplate;
import com.medical.imaging.repository.ReportRepository;
import com.medical.imaging.repository.ReportTemplateRepository;
import com.medical.imaging.service.ReportService;
import com.medical.imaging.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportTemplateRepository templateRepository;

    @Override
    @Transactional
    public ReportDTO generateReport(ReportGenerateRequest request) {
        ReportTemplate template = templateRepository.findById(request.getTemplateId())
            .orElseThrow(() -> new ResourceNotFoundException("Report template not found"));

        Report report = new Report();
        report.setStudyId(request.getStudyId());
        report.setTitle(request.getTitle());
        report.setContent(request.getContent() != null ? request.getContent() : template.getContent());
        report.setConclusion(request.getConclusion());
        report.setDoctorName(request.getDoctorName());
        report.setStatus("DRAFT");

        report = reportRepository.save(report);
        return convertToDTO(report);
    }

    @Override
    @Transactional
    public ReportDTO updateReport(Long reportId, ReportUpdateRequest request) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (request.getTitle() != null) {
            report.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            report.setContent(request.getContent());
        }
        if (request.getConclusion() != null) {
            report.setConclusion(request.getConclusion());
        }
        if (request.getDoctorName() != null) {
            report.setDoctorName(request.getDoctorName());
        }
        if (request.getStatus() != null) {
            report.setStatus(request.getStatus());
        }

        report = reportRepository.save(report);
        return convertToDTO(report);
    }

    // ... 其他方法的实现

    private ReportDTO convertToDTO(Report report) {
        return ReportDTO.builder()
            .id(report.getId())
            .studyId(report.getStudyId())
            .title(report.getTitle())
            .content(report.getContent())
            .conclusion(report.getConclusion())
            .doctorName(report.getDoctorName())
            .status(report.getStatus())
            .createdAt(report.getCreatedAt())
            .updatedAt(report.getUpdatedAt())
            .createdBy(report.getCreatedBy())
            .updatedBy(report.getUpdatedBy())
            .build();
    }
} 