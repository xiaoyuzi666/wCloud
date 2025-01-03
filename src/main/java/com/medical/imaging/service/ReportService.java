package com.medical.imaging.service;

import com.medical.imaging.dto.report.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    ReportDTO generateReport(ReportGenerateRequest request);
    ReportDTO updateReport(Long reportId, ReportUpdateRequest request);
    void deleteReport(Long reportId);
    ReportDTO getReport(Long reportId);
    Page<ReportDTO> getReportsByStudy(Long studyId, Pageable pageable);
    ReportTemplateDTO createTemplate(ReportTemplateRequest request);
    ReportTemplateDTO updateTemplate(Long templateId, ReportTemplateRequest request);
    void deleteTemplate(Long templateId);
    ReportTemplateDTO getTemplate(Long templateId);
    Page<ReportTemplateDTO> getAllTemplates(Pageable pageable);
} 