package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Report;

import java.util.Collection;

public interface IReportService {
    Collection<Report> getAll();
    Report findReport(Long reportId);
    Report insert(Report report);
    Report update(Report report);
    Report delete(Long reportId);
    void deleteAll();
    
}
