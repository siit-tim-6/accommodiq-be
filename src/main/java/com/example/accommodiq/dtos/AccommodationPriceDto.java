package com.example.accommodiq.dtos;

public class AccommodationPriceDto { // valid
    private double totalPrice;

    public AccommodationPriceDto() {
        super();
    }

    public AccommodationPriceDto(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
