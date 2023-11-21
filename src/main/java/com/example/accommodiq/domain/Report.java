package com.example.accommodiq.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String reason;
    private Date date;
    @ManyToOne(cascade = CascadeType.ALL)
    private User from;
    @ManyToOne(cascade = CascadeType.ALL)
    private User to;

    public Report() {}

    public Report(Long id, String reason, Date date, User from, User to) {
        this.id = id;
        this.reason = reason;
        this.date = date;
        this.from = from;
        this.to = to;
    }

    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public Date getDate() {
        return date;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void setTo(User to) {
        this.to = to;
    }


}
