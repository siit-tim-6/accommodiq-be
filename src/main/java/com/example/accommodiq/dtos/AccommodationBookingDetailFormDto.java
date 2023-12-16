package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.enums.PricingType;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

public class AccommodationBookingDetailFormDto {
    private int cancellationDeadline;
    private PricingType pricingType;
    private List<Availability> available = new ArrayList<>();

    public AccommodationBookingDetailFormDto() {
        super();
    }

    public AccommodationBookingDetailFormDto(int cancellationDeadline, PricingType pricingType, List<Availability> available) {
        this.cancellationDeadline = cancellationDeadline;
        this.pricingType = pricingType;
        this.available = available;
    }

    public AccommodationBookingDetailFormDto(Accommodation accommodation) {
        Hibernate.initialize(accommodation.getAvailable());
        this.cancellationDeadline = accommodation.getCancellationDeadline();
        this.pricingType = accommodation.getPricingType();
        this.available = new ArrayList<>(accommodation.getAvailable());
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public PricingType getPricingType() {
        return pricingType;
    }

    public void setPricingType(PricingType pricingType) {
        this.pricingType = pricingType;
    }

    public List<Availability> getAvailable() {
        return available;
    }

    public void setAvailable(List<Availability> available) {
        this.available = available;
    }
}
