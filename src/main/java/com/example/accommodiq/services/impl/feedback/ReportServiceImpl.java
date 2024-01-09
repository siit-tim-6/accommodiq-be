package com.example.accommodiq.services.impl.feedback;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Report;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.MessageDto;
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
import org.springframework.security.core.Authentication;
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
    public MessageDto reportUser(Long reportedUserId, ReportDto reportDto) {
        Account reportingUserAccount = getLoggedInAccount();
        Account reportedUserAccount =  accountService.findAccount(reportedUserId);

        validateReportInput(reportedUserAccount,reportingUserAccount, reportDto);

        if (!hasPastReservationBetweenUsers(reportedUserAccount, reportingUserAccount)) {
            throw new IllegalStateException("Cannot report user without a past reservation.");
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

    private void validateReportInput(Account reportedUser, Account reportingUser, ReportDto reportDto) {
        if (Objects.equals(reportedUser.getId(), reportingUser.getId())) {
            throw generateBadRequest("reportYourself");
        }
        if (reportedUser.getId() == null || reportingUser.getId() == null) {
            throw generateBadRequest("reportNull");
        }
        if (reportDto.getReason() == null || reportDto.getReason().isEmpty()) {
            throw generateBadRequest("reportReasonNull");
        }
        if (reportedUser.getRole() == reportingUser.getRole()) {
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

    private boolean hasPastReservationBetweenUsers(Account firstAccount, Account secondAccount) {
        boolean hasReservation;
        if (firstAccount.getRole() == AccountRole.HOST) {
            hasReservation = reservationService.hasPastReservation(firstAccount.getId(), secondAccount.getId());
        } else {
            hasReservation = reservationService.hasPastReservation(secondAccount.getId(), firstAccount.getId());
        }
        return hasReservation;
    }

    private Account getLoggedInAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return (Account) accountService.loadUserByUsername(email);
    }
}
