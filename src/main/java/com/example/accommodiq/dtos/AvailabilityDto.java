package com.example.accommodiq.dtos;

import jakarta.validation.constraints.NotNull;

public class AvailabilityDto {
    @NotNull(message = "From date is required")
    private Long fromDate;
    @NotNull(message = "To date is required")
    private Long toDate;
    @NotNull(message = "Price is required")
    private double price;

    public AvailabilityDto() {
        super();
    }

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
