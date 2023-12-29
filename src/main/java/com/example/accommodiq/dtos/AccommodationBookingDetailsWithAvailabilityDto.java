package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.enums.PricingType;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

public class AccommodationBookingDetailsWithAvailabilityDto extends AccommodationBookingDetailsDto{
    private List<Availability> available = new ArrayList<>();

    public AccommodationBookingDetailsWithAvailabilityDto() {
        super();
    }

    public AccommodationBookingDetailsWithAvailabilityDto(int cancellationDeadline, PricingType pricingType, List<Availability> available) {
        super(cancellationDeadline, pricingType);
        this.available = available;
    }

    public AccommodationBookingDetailsWithAvailabilityDto(Accommodation accommodation) {
        super(accommodation.getCancellationDeadline(), accommodation.getPricingType());
        Hibernate.initialize(accommodation.getAvailable());
        this.available = new ArrayList<>(accommodation.getAvailable());
    }

    public List<Availability> getAvailable() {
        return available;
    }

    public void setAvailable(List<Availability> available) {
        this.available = available;
    }
}
