package com.example.accommodiq.services;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.repositories.ReportRepository;
import com.example.accommodiq.services.interfaces.IReportService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class ReportServiceImpl  implements IReportService {

    final
    ReportRepository allReports;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
    public ReportServiceImpl(ReportRepository allReports) {
        this.allReports = allReports;
    }

    @Override
    public Collection<Report> getAll() {
        return allReports.findAll();
    }

    @Override
    public Report findReport(Long reportId) {
        Optional<Report> found = allReports.findById(reportId);
        if (found.isEmpty()) {
            String value = bundle.getString("reportNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Report insert(Report report) {
        try {
            allReports.save(report);
            allReports.flush();
            return report;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report cannot be inserted");
        }
    }

    @Override
    public Report update(Report report) {
        try {
            findReport(report.getId()); // this will throw ResponseStatusException if report is not found
            allReports.save(report);
            allReports.flush();
            return report;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report cannot be updated");
        }
    }

    @Override
    public Report delete(Long reportId) {
        Report report = findReport(reportId);
        allReports.delete(report);
        allReports.flush();
        return report;
    }

    @Override
    public void deleteAll() {
        allReports.deleteAll();
        allReports.flush();
    }

    @Override
    public void reportUser(Long reportedUserId, ReportDto reportDto) {
    }


}
