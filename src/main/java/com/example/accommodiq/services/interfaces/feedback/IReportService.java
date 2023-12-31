package com.example.accommodiq.services.interfaces.feedback;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.dtos.ReportModificationDto;

import java.util.Collection;

public interface IReportService {
    Collection<Report> getAll();

    Report findReport(Long reportId);

    Report insert(Report report);

    ReportModificationDto insert(ReportModificationDto report);

    ReportModificationDto update(ReportModificationDto report);

    Report delete(Long reportId);

    void deleteAll();

    void reportUser(Long reportedUserId,Long reportingUserId, ReportDto reportDto);

    void deleteByReportedUserId(Long id);

    void deleteByReportingUserId(Long id);
    
}
