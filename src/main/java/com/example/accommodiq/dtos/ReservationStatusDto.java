package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;

public class ReservationStatusDto {
    @NotNull(message = "Status is required")
    private ReservationStatus status;

    public ReservationStatusDto(ReservationStatus status) {
        this.status = status;
    }

    public ReservationStatusDto() {
        super();
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
