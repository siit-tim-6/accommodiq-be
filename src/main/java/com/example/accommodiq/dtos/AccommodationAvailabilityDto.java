package com.example.accommodiq.dtos;

public class AccommodationAvailabilityDto {
    private boolean isAvailable;

    public AccommodationAvailabilityDto() {}

    public AccommodationAvailabilityDto(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
