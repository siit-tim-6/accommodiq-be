package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.ReservationStatus;

public class ReservationStatusDto {
    private ReservationStatus status;

    public ReservationStatusDto(ReservationStatus status) {
        this.status = status;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
