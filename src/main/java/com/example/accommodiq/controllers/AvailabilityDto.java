package com.example.accommodiq.controllers;

import java.util.Date;

public class AvailabilityDto {
    private Date fromDate;
    private Date toDate;
    private double price;

    public AvailabilityDto(Date fromDate, Date toDate, double price) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.price = price;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
