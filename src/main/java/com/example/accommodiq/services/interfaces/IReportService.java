package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.dtos.ReportRequestDto;
import com.example.accommodiq.dtos.ReportResponseDto;

import java.util.Collection;

public interface IReportService {
    Collection<Report> getAll();

    Report findReport(Long reportId);

    Report insert(Report report);

    ReportResponseDto insert(ReportRequestDto report);

    ReportResponseDto update(ReportRequestDto report);

    Report delete(Long reportId);

    void deleteAll();

    void reportUser(Long reportedUserId, ReportDto reportDto);
    
}
