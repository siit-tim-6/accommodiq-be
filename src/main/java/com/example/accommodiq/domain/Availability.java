package com.example.accommodiq.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long fromDate;
    private Long toDate;
    private double price;

    public Availability(Long id, Long fromDate, Long toDate, double price) {
        super();
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
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

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long from) {
        this.fromDate = from;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long to) {
        this.toDate = to;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
