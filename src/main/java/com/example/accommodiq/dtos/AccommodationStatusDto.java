package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.AccommodationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccommodationStatusDto {
    @NotNull(message = "Status is required")
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
