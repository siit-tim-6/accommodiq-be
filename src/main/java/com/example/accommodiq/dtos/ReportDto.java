package com.example.accommodiq.dtos;

import jakarta.validation.constraints.NotBlank;

public class ReportDto {
    @NotBlank(message = "Reason for reporting is required")
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
