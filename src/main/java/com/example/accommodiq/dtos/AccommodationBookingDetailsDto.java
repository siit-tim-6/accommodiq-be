package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.enums.PricingType;

public class AccommodationBookingDetailsDto {
    private int cancellationDeadline;
    private PricingType pricingType;

    public AccommodationBookingDetailsDto() {
        super();
    }

    public AccommodationBookingDetailsDto(int cancellationDeadline, PricingType pricingType) {
        this.cancellationDeadline = cancellationDeadline;
        this.pricingType = pricingType;
    }

    public AccommodationBookingDetailsDto(Accommodation accommodation) {
        this.cancellationDeadline = accommodation.getCancellationDeadline();
        this.pricingType = accommodation.getPricingType();
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
}
