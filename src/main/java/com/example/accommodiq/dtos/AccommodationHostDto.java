package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.enums.AccommodationStatus;

public class AccommodationHostDto extends AccommodationListDto {
    private AccommodationStatus status;

    public AccommodationHostDto(Accommodation accommodation) {
        super(accommodation);
        this.status = accommodation.getStatus();
    }

    public AccommodationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatus status) {
        this.status = status;
    }

}
