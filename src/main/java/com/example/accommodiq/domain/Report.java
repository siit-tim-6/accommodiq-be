package com.example.accommodiq.domain;

import jakarta.persistence.*;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String reason;
    private Long time;
    @ManyToOne(cascade = CascadeType.ALL)
    private User reportingUser;
    @ManyToOne(cascade = CascadeType.ALL)
    private User reportedUser;

    public Report() {}

    public Report(Long id, String reason, Long time, User reportingUser, User reportedUser) {
        this.id = id;
        this.reason = reason;
        this.time = time;
        this.reportingUser = reportingUser;
        this.reportedUser = reportedUser;
    }

    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public Long getTime() {
        return time;
    }

    public User getReportingUser() {
        return reportingUser;
    }

    public User getTo() {
        return reportedUser;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }


}
