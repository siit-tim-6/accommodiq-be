package com.example.accommodiq.services;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.dtos.ReportModificationDto;
import com.example.accommodiq.repositories.ReportRepository;
import com.example.accommodiq.services.interfaces.IReportService;
import com.example.accommodiq.services.interfaces.IUserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static com.example.accommodiq.utilities.ErrorUtils.generateBadRequest;
import static com.example.accommodiq.utilities.ErrorUtils.generateNotFound;

@Service
public class ReportServiceImpl implements IReportService {

    final ReportRepository allReports;

    final IUserService userService;

    @Autowired
    public ReportServiceImpl(ReportRepository allReports, IUserService userService) {
        this.allReports = allReports;
        this.userService = userService;
    }

    @Override
    public Collection<Report> getAll() {
        return allReports.findAll();
    }

    @Override
    public Report findReport(Long reportId) {
        Optional<Report> found = allReports.findById(reportId);
        if (found.isEmpty()) {
            generateNotFound("reportNotFound");
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
            generateBadRequest("reportInsertFailed");
        }
        return report;
    }

    @Override
    public ReportModificationDto insert(ReportModificationDto reportDto) {
        Report report = convertToReport(reportDto);
        try {
            allReports.save(report);
            allReports.flush();
            return new ReportModificationDto(report);
        } catch (ConstraintViolationException ex) {
            generateBadRequest("reportInsertFailed");
        }
        return new ReportModificationDto(report);
    }

    @Override
    public ReportModificationDto update(ReportModificationDto reportModificationDto) {
        Report existingReport = findReport(reportModificationDto.getId());

        existingReport.setReason(reportModificationDto.getReason());
        existingReport.setTimestamp(reportModificationDto.getTimestamp());

        try {
            findReport(existingReport.getId()); // this will throw ResponseStatusException if report is not found
            allReports.save(existingReport);
            allReports.flush();
            return new ReportModificationDto(existingReport);
        } catch (ConstraintViolationException ex) {
            generateBadRequest("reportUpdateFailed");
        }
        return new ReportModificationDto(existingReport);
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

        validateReportInput(reportedUserId, reportDto);

        Report report = new Report();
        report.setReportedUser(userService.findUser(reportedUserId));
        report.setReason(reportDto.getReason());
        report.setReportingUser(userService.findUser(reportDto.getReportingUserId()));
        report.setTimestamp(reportDto.getTimestamp());
        insert(report);
    }

    @Override
    public void deleteByReportedUserId(Long id) {
        allReports.deleteByReportedUserId(id);
        allReports.flush();
    }

    @Override
    public void deleteByReportingUserId(Long id) {
        allReports.deleteByReportingUserId(id);
        allReports.flush();
    }

    private void validateReportInput(Long reportedUserId, ReportDto reportDto) {
        if (Objects.equals(reportedUserId, reportDto.getReportingUserId())) {
            generateBadRequest("reportYourself");
        }
        if (reportedUserId == null || reportDto.getReportingUserId() == null) {
            generateBadRequest("reportNull");
        }
        if (reportDto.getReason() == null || reportDto.getReason().isEmpty()) {
            generateBadRequest("reportReasonNull");
        }
    }

    private Report convertToReport(ReportModificationDto reportDto) {
        Report report = new Report();
        report.setReportedUser(userService.findUser(reportDto.getReportedUserId()));
        report.setReason(reportDto.getReason());
        report.setReportingUser(userService.findUser(reportDto.getReportingUserId()));
        report.setTimestamp(reportDto.getTimestamp());
        return report;
    }
}
