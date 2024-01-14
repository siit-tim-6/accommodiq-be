package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Account;

public class ReportCardDto {
    Report_UserInfoDto reportingUser;
    Report_UserInfoDto reportedUser;
    String reason;
    Long id;

    public ReportCardDto() {
        super();
    }

    public ReportCardDto(Account reportedUserAccount, Account reportingUserAccount, String reason, Long id) {
        this.reportingUser = new Report_UserInfoDto(reportingUserAccount);
        this.reportedUser = new Report_UserInfoDto(reportedUserAccount);
        this.reason = reason;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public Report_UserInfoDto getReportingUser() {
        return reportingUser;
    }

    public Report_UserInfoDto getReportedUser() {
        return reportedUser;
    }
}
