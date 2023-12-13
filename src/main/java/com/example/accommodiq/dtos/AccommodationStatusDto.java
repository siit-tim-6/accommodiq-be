package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.AccommodationStatus;

public class AccommodationStatusDto {
    private AccommodationStatus status;

    public AccommodationStatusDto() {
        super();
    }

    public AccommodationStatusDto(AccommodationStatus status) {
        this.status = status;
    }

    public AccommodationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatus status) {
        this.status = status;
    }
}
