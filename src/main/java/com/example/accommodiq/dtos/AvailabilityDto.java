package com.example.accommodiq.dtos;

import java.util.Date;

public class AvailabilityDto {
    private Long fromDate;
    private Long toDate;
    private double price;

    public AvailabilityDto(Long fromDate, Long toDate, double price) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.price = price;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
