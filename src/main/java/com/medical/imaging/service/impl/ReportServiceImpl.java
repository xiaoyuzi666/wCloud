package com.medical.imaging.service.impl;

import com.medical.imaging.dto.report.*;
import com.medical.imaging.entity.Report;
import com.medical.imaging.entity.ReportTemplate;
import com.medical.imaging.repository.ReportRepository;
import com.medical.imaging.repository.ReportTemplateRepository;
import com.medical.imaging.service.ReportService;
import com.medical.imaging.exception.ResourceNotFoundException;
import com.medical.imaging.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportTemplateRepository templateRepository;

    @Override
    @Transactional
    public ReportDTO generateReport(ReportGenerateRequest request) {
        Report report = new Report();
        report.setStudyId(request.getStudyId());
        report.setTitle(request.getTitle());
        report.setContent(request.getContent());
        report.setConclusion(request.getConclusion());
        report.setDoctorName(request.getDoctorName());
        report.setStatus("DRAFT");

        report = reportRepository.save(report);
        return convertToDTO(report);
    }

    @Override
    public ReportDTO getReport(Long reportId) {
        return reportRepository.findById(reportId)
            .map(this::convertToDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
    }

    @Override
    public Page<ReportDTO> getReportsByStudy(Long studyId, Pageable pageable) {
        return reportRepository.findByStudyId(studyId, pageable)
            .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public void deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
            
        if ("FINALIZED".equals(report.getStatus())) {
            throw new BusinessException("Cannot delete finalized report");
        }
        
        reportRepository.delete(report);
    }

    @Override
    @Transactional
    public ReportDTO updateReport(Long reportId, ReportUpdateRequest request) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
            
        if ("FINALIZED".equals(report.getStatus())) {
            throw new BusinessException("Cannot update finalized report");
        }

        if (request.getTitle() != null) {
            report.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            report.setContent(request.getContent());
        }
        if (request.getConclusion() != null) {
            report.setConclusion(request.getConclusion());
        }
        if (request.getStatus() != null) {
            report.setStatus(request.getStatus());
        }

        report = reportRepository.save(report);
        return convertToDTO(report);
    }

    @Override
    @Transactional
    public ReportTemplateDTO createTemplate(ReportTemplateRequest request) {
        if (request.isDefault()) {
            templateRepository.findByIsDefaultTrue()
                .ifPresent(template -> {
                    template.setDefault(false);
                    templateRepository.save(template);
                });
        }

        ReportTemplate template = new ReportTemplate();
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setContent(request.getContent());
        template.setCategory(request.getCategory());
        template.setDefault(request.isDefault());

        template = templateRepository.save(template);
        return convertToTemplateDTO(template);
    }

    @Override
    @Transactional
    public ReportTemplateDTO updateTemplate(Long templateId, ReportTemplateRequest request) {
        ReportTemplate template = templateRepository.findById(templateId)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        if (request.getName() != null) {
            if (templateRepository.existsByNameAndIdNot(request.getName(), templateId)) {
                throw new BusinessException("Template name already exists");
            }
            template.setName(request.getName());
        }

        if (request.getDescription() != null) {
            template.setDescription(request.getDescription());
        }
        if (request.getContent() != null) {
            template.setContent(request.getContent());
        }
        if (request.getCategory() != null) {
            template.setCategory(request.getCategory());
        }

        if (request.isDefault() && !template.isDefault()) {
            templateRepository.findByIsDefaultTrue()
                .ifPresent(defaultTemplate -> {
                    defaultTemplate.setDefault(false);
                    templateRepository.save(defaultTemplate);
                });
            template.setDefault(true);
        }

        template = templateRepository.save(template);
        return convertToTemplateDTO(template);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long templateId) {
        ReportTemplate template = templateRepository.findById(templateId)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
            
        if (template.isDefault()) {
            throw new BusinessException("Cannot delete default template");
        }
        
        templateRepository.delete(template);
    }

    @Override
    public Page<ReportTemplateDTO> getTemplates(Pageable pageable) {
        return templateRepository.findAll(pageable)
            .map(this::convertToTemplateDTO);
    }

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

    private ReportTemplateDTO convertToTemplateDTO(ReportTemplate template) {
        return ReportTemplateDTO.builder()
            .id(template.getId())
            .name(template.getName())
            .description(template.getDescription())
            .content(template.getContent())
            .category(template.getCategory())
            .isDefault(template.isDefault())
            .createdAt(template.getCreatedAt())
            .updatedAt(template.getUpdatedAt())
            .createdBy(template.getCreatedBy())
            .updatedBy(template.getUpdatedBy())
            .build();
    }

    // ... 其他方法的实现
} 