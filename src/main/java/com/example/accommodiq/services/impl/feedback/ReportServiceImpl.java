package com.example.accommodiq.services.impl.feedback;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.dtos.ReportModificationDto;
import com.example.accommodiq.repositories.ReportRepository;
import com.example.accommodiq.services.interfaces.feedback.IReportService;
import com.example.accommodiq.services.interfaces.users.IUserService;
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
            throw generateNotFound("reportNotFound");
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
            throw generateBadRequest("reportInsertFailed");
        }
    }

    @Override
    public ReportModificationDto insert(ReportModificationDto reportDto) {
        Report report = convertToReport(reportDto);
        try {
            allReports.save(report);
            allReports.flush();
            return new ReportModificationDto(report);
        } catch (ConstraintViolationException ex) {
            throw generateBadRequest("reportInsertFailed");
        }
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
            throw generateBadRequest("reportUpdateFailed");
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
    public void reportUser(Long reportedUserId,Long reportingUserId, ReportDto reportDto) {
        validateReportInput(reportedUserId,reportingUserId, reportDto);

        User reportedUser = userService.findUser(reportedUserId);
        User reportingUser = userService.findUser(reportingUserId);
        Report report = new Report(reportedUser, reportingUser, reportDto);
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

    private void validateReportInput(Long reportedUserId, Long reportingUserId, ReportDto reportDto) {
        if (Objects.equals(reportedUserId, reportingUserId)) {
            throw generateBadRequest("reportYourself");
        }
        if (reportedUserId == null || reportingUserId == null) {
            throw generateBadRequest("reportNull");
        }
        if (reportDto.getReason() == null || reportDto.getReason().isEmpty()) {
            throw generateBadRequest("reportReasonNull");
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
