package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Account;

public class ReportCardDto {
    ReportUserInfoDto reportingUser;
    ReportUserInfoDto reportedUser;
    String reason;

    public ReportCardDto() {
        super();
    }

    public ReportCardDto(Account reportedUserAccount, Account reportingUserAccount, String reason) {
        this.reportingUser = new ReportUserInfoDto(reportingUserAccount);
        this.reportedUser = new ReportUserInfoDto(reportedUserAccount);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public ReportUserInfoDto getReportingUser() {
        return reportingUser;
    }

    public ReportUserInfoDto getReportedUser() {
        return reportedUser;
    }
}
