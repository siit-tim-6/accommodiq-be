package com.example.accommodiq.dtos;

import java.util.Date;

public class ReportDto {
    private Long id;
    private String reason;
    private Long reportingUserId;
    private Date date;

    public ReportDto(String reason) {
        this.reason = reason;
    }

    public ReportDto() {
    }

    public String getReason() {
        return reason;
    }
}
