package com.example.accommodiq.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date from;
    private Date to;
    private double price;

    public Availability(Long id, Date from, Date to, double price) {
        super();
        this.id = id;
        this.from = from;
        this.to = to;
        this.price = price;
    }

    public Availability() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
