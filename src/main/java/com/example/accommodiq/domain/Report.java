package com.example.accommodiq.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String reason;
    private Long time;
    @ManyToOne(cascade = CascadeType.ALL)
    private User from;
    @ManyToOne(cascade = CascadeType.ALL)
    private User to;

    public Report() {}

    public Report(Long id, String reason, Long time, User from, User to) {
        this.id = id;
        this.reason = reason;
        this.time = time;
        this.from = from;
        this.to = to;
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

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
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

    public void setFrom(User from) {
        this.from = from;
    }

    public void setTo(User to) {
        this.to = to;
    }


}
