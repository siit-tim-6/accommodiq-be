package com.example.accommodiq.domain;

import com.example.accommodiq.dtos.ReportDto;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String reason;
    private Long timestamp;
    @ManyToOne(fetch = FetchType.LAZY)
    private User reportingUser;
    @ManyToOne(fetch = FetchType.LAZY)
    private User reportedUser;

    public Report() {}

    public Report(Long id, String reason, Long timestamp, User reportingUser, User reportedUser) {
        this.id = id;
        this.reason = reason;
        this.timestamp = timestamp;
        this.reportingUser = reportingUser;
        this.reportedUser = reportedUser;
    }

    public Report(User reportedUser, User reportingUser, ReportDto reportDto) {
        this.reason = reportDto.getReason();
        this.timestamp = Instant.now().toEpochMilli();
        this.reportingUser = reportingUser;
        this.reportedUser = reportedUser;
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

    public User getReportingUser() {
        return reportingUser;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }


}
