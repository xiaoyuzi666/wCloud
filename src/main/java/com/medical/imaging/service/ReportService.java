package com.medical.imaging.service;

import com.medical.imaging.dto.report.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    ReportDTO generateReport(ReportGenerateRequest request);
    ReportDTO getReport(Long reportId);
    Page<ReportDTO> getReportsByStudy(Long studyId, Pageable pageable);
    void deleteReport(Long reportId);
    ReportDTO updateReport(Long reportId, ReportUpdateRequest request);
    byte[] exportReportAsPdf(Long reportId);
    ReportTemplateDTO createTemplate(ReportTemplateRequest request);
    ReportTemplateDTO updateTemplate(Long templateId, ReportTemplateRequest request);
    void deleteTemplate(Long templateId);
    Page<ReportTemplateDTO> getTemplates(Pageable pageable);
} 