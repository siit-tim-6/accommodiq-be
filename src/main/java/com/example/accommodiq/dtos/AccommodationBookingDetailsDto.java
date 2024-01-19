package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.enums.PricingType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AccommodationBookingDetailsDto {
    @NotNull(message = "Cancellation deadline is required")
    @Min(value = 0, message = "Cancellation deadline must be greater than or equal to 0")
    private int cancellationDeadline;
    @NotNull(message = "Pricing type is required")
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
