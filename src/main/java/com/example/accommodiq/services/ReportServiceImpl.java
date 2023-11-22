package com.example.accommodiq.services;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.repositories.ReportRepository;
import com.example.accommodiq.services.interfaces.IReportService;
import com.example.accommodiq.services.interfaces.IUserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static com.example.accommodiq.utilities.ReportUtils.throwBadRequest;

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
            throwBadRequest("reportNotFound");
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
            throwBadRequest("reportInsertFailed");
        }
        return report;
    }

    @Override
    public Report update(Report report) {
        try {
            findReport(report.getId()); // this will throw ResponseStatusException if report is not found
            allReports.save(report);
            allReports.flush();
            return report;
        } catch (ConstraintViolationException ex) {
            throwBadRequest("reportUpdateFailed");
        }
        return report;
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
        report.setTimestamp(Instant.now().toEpochMilli());
        insert(report);
    }

    private void validateReportInput(Long reportedUserId, ReportDto reportDto) {
        if (Objects.equals(reportedUserId, reportDto.getReportingUserId())) {
            throwBadRequest("reportYourself");
        }
        if (reportedUserId == null || reportDto.getReportingUserId() == null) {
            throwBadRequest("reportNull");
        }
        if (reportDto.getReason() == null || reportDto.getReason().isEmpty()) {
            throwBadRequest("reportReasonNull");
        }
    }


}
