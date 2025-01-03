package com.medical.imaging.service.impl;

import com.medical.imaging.dto.report.*;
import com.medical.imaging.entity.Report;
import com.medical.imaging.entity.ReportTemplate;
import com.medical.imaging.entity.Study;
import com.medical.imaging.exception.ResourceNotFoundException;
import com.medical.imaging.repository.ReportRepository;
import com.medical.imaging.repository.ReportTemplateRepository;
import com.medical.imaging.repository.StudyRepository;
import com.medical.imaging.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final StudyRepository studyRepository;
    private final ReportTemplateRepository templateRepository;

    @Override
    @Transactional
    public ReportDTO createReport(Long studyId, ReportGenerateRequest request) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new ResourceNotFoundException("Study not found"));

        ReportTemplate template = templateRepository.findById(request.getTemplateId())
            .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        Report report = new Report();
        report.setStudy(study);
        report.setContent(request.getContent());
        report.setFindings(request.getFindings());
        report.setConclusion(request.getConclusion());
        report.setRecommendation(request.getRecommendation());
        report.setDoctorName(request.getDoctorName());
        report.setTemplateId(template.getId());
        report.setStatus("DRAFT");

        return convertToDTO(reportRepository.save(report));
    }

    @Override
    public ReportDTO getReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        return convertToDTO(report);
    }

    @Override
    @Transactional
    public ReportDTO updateReport(Long reportId, ReportUpdateRequest request) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        report.setContent(request.getContent());
        report.setFindings(request.getFindings());
        report.setConclusion(request.getConclusion());
        report.setRecommendation(request.getRecommendation());
        report.setStatus(request.getStatus());

        return convertToDTO(reportRepository.save(report));
    }

    @Override
    @Transactional
    public void deleteReport(Long reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new ResourceNotFoundException("Report not found");
        }
        reportRepository.deleteById(reportId);
    }

    @Override
    public Page<ReportDTO> getStudyReports(Long studyId, Pageable pageable) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new ResourceNotFoundException("Study not found"));
        return reportRepository.findByStudy(study, pageable)
            .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public ReportTemplateDTO createTemplate(ReportTemplateRequest request) {
        ReportTemplate template = new ReportTemplate();
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setModality(request.getModality());
        template.setTemplate(request.getTemplate());
        template.setIsDefault(request.getIsDefault());

        return convertToTemplateDTO(templateRepository.save(template));
    }

    @Override
    @Transactional
    public ReportTemplateDTO updateTemplate(Long templateId, ReportTemplateRequest request) {
        ReportTemplate template = templateRepository.findById(templateId)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setModality(request.getModality());
        template.setTemplate(request.getTemplate());
        template.setIsDefault(request.getIsDefault());

        return convertToTemplateDTO(templateRepository.save(template));
    }

    @Override
    @Transactional
    public void deleteTemplate(Long templateId) {
        if (!templateRepository.existsById(templateId)) {
            throw new ResourceNotFoundException("Template not found");
        }
        templateRepository.deleteById(templateId);
    }

    @Override
    public ReportTemplateDTO getTemplate(Long templateId) {
        ReportTemplate template = templateRepository.findById(templateId)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        return convertToTemplateDTO(template);
    }

    @Override
    public Page<ReportTemplateDTO> getAllTemplates(Pageable pageable) {
        return templateRepository.findAll(pageable)
            .map(this::convertToTemplateDTO);
    }

    private ReportDTO convertToDTO(Report report) {
        return ReportDTO.builder()
            .id(report.getId())
            .studyId(report.getStudy().getId())
            .content(report.getContent())
            .status(report.getStatus())
            .conclusion(report.getConclusion())
            .findings(report.getFindings())
            .recommendation(report.getRecommendation())
            .doctorName(report.getDoctorName())
            .templateId(report.getTemplateId())
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
            .modality(template.getModality())
            .template(template.getTemplate())
            .isDefault(template.getIsDefault())
            .createdAt(template.getCreatedAt())
            .createdBy(template.getCreatedBy())
            .build();
    }
} 