package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.PricingType;

import java.util.Set;

public class AvailabilityPricingDto {
    private int cancellationDeadline;
    private PricingType pricingType;
    private Set<AvailabilityDto> available;

    public AvailabilityPricingDto() {
        super();
    }

    public AvailabilityPricingDto(int cancellationDeadline, PricingType pricingType, Set<AvailabilityDto> available) {
        this.cancellationDeadline = cancellationDeadline;
        this.pricingType = pricingType;
        this.available = available;
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

    public Set<AvailabilityDto> getAvailable() {
        return available;
    }

    public void setAvailable(Set<AvailabilityDto> available) {
        this.available = available;
    }
}
