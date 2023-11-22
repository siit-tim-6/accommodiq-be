package com.example.accommodiq.dtos;

public class ReportDto {
    private Long id;
    private String reason;
    private Long reportingUserId;
    private Long time;

    public ReportDto(String reason) {
        this.reason = reason;
    }

    public ReportDto() {
    }

    public String getReason() {
        return reason;
    }

    public Long getId() {
        return id;
    }

    public Long getReportingUserId() {
        return reportingUserId;
    }

    public Long getTime() {
        return time;
    }

}
