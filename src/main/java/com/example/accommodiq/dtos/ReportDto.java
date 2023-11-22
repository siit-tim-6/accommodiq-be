package com.example.accommodiq.dtos;

public class ReportDto {
    private Long id;
    private String reason;
    private Long reportingUserId;
    private Long timestamp;

    public ReportDto(Long id, String reason, Long reportingUserId, Long timestamp) {
        this.id = id;
        this.reason = reason;
        this.reportingUserId = reportingUserId;
        this.timestamp = timestamp;
    }

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

    public Long getTimestamp() {
        return timestamp;
    }

}
