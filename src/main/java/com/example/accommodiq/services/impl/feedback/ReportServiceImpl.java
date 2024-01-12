package com.example.accommodiq.services.impl.feedback;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReportCardDto;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.dtos.ReportModificationDto;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.repositories.ReportRepository;
import com.example.accommodiq.services.interfaces.accommodations.IReservationService;
import com.example.accommodiq.services.interfaces.feedback.IReportService;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.users.IUserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    final IReservationService reservationService;

    final IAccountService accountService;

    @Autowired
    public ReportServiceImpl(ReportRepository allReports, IUserService userService, IReservationService reservationService, IAccountService accountService) {
        this.allReports = allReports;
        this.userService = userService;
        this.reservationService = reservationService;
        this.accountService = accountService;
    }

    @Override
    public Collection<ReportCardDto> getAll() {
        return allReports.findAll().stream().map(this::getReportCardDto).toList();
    }

    private ReportCardDto getReportCardDto(Report report) {
        Account reportedUserAccount = accountService.findAccount(report.getReportedUser().getId());
        Account reportingUserAccount = accountService.findAccount(report.getReportingUser().getId());
        return new ReportCardDto(reportedUserAccount, reportingUserAccount, report.getReason());
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
    public MessageDto reportUser(Long reportedUserId, ReportDto reportDto) {
        Account reportingUserAccount = getLoggedInAccount();
        Account reportedUserAccount =  accountService.findAccount(reportedUserId);

        validateReportInput(reportedUserAccount,reportingUserAccount, reportDto);

        Collection<Reservation> pastReservations = getPastReservationBetweenUsers(reportedUserAccount, reportingUserAccount);
        if (pastReservations.isEmpty()) {
            throw new IllegalStateException("Cannot report user without a past reservation.");
        }

        Collection<Report> reports = allReports.findAllByReportedUserIdAndReportingUserId(reportedUserId, reportingUserAccount.getId());
        if(reports.size() >= pastReservations.size()) {
            throw new IllegalStateException("Cannot report user more than once per reservation.");
        }

        User reportedUser = reportedUserAccount.getUser();
        User reportingUser = reportingUserAccount.getUser();
        Report report = new Report(reportedUser, reportingUser, reportDto);
        insert(report);
        return new MessageDto("Reported Successfully");
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

    private void validateReportInput(Account reportedAccount, Account reportingAccount, ReportDto reportDto) {
        if (Objects.equals(reportedAccount.getId(), reportingAccount.getId())) {
            throw generateBadRequest("reportYourself");
        }
        if (reportedAccount.getId() == null || reportingAccount.getId() == null) {
            throw generateBadRequest("reportNull");
        }
        if (reportDto.getReason() == null || reportDto.getReason().isEmpty()) {
            throw generateBadRequest("reportReasonNull");
        }
        if (reportedAccount.getRole() == reportingAccount.getRole()) {
            throw generateBadRequest("reportSameRole");
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

    private Collection<Reservation> getPastReservationBetweenUsers(Account firstAccount, Account secondAccount) {
        if (firstAccount.getRole() == AccountRole.HOST) {
            return reservationService.getPastReservations(firstAccount.getId(), secondAccount.getId());
        }
        return reservationService.getPastReservations(secondAccount.getId(), firstAccount.getId());
    }

    private Account getLoggedInAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return (Account) accountService.loadUserByUsername(email);
    }
}
