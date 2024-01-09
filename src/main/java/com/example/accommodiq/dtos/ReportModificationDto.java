package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Report;

public class ReportModificationDto {
    private Long id;
    private String reason;
    private Long timestamp;
    private Long reportingUserId;
    private Long reportedUserId;

    public ReportModificationDto(Long id, String reason, Long timestamp, Long reportingUserId, Long reportedUserId) {
        this.id = id;
        this.reason = reason;
        this.timestamp = timestamp;
        this.reportingUserId = reportingUserId;
        this.reportedUserId = reportedUserId;
    }

    public ReportModificationDto(Report report) {
        this.id = report.getId();
        this.reason = report.getReason();
        this.timestamp = report.getTimestamp();
        this.reportingUserId = report.getReportingUser().getId();
        this.reportedUserId = report.getReportedUser().getId();
    }

    public ReportModificationDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getReportingUserId() {
        return reportingUserId;
    }

    public Long getReportedUserId() {
        return reportedUserId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setReportingUserId(Long reportingUserId) {
        this.reportingUserId = reportingUserId;
    }

    public void setReportedUserId(Long reportedUserId) {
        this.reportedUserId = reportedUserId;
    }
}
