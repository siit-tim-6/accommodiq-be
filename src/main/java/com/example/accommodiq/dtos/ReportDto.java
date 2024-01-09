package com.example.accommodiq.dtos;

public class ReportDto {
    private String reason;

    public ReportDto(String reason) {
        this.reason = reason;
    }

    public ReportDto() {
        super();
    }

    public String getReason() {
        return reason;
    }
}
