package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.enums.ReservationStatus;

public class ReservationDto {
    private Long id;
    private Long startDate;
    private Long endDate;
    private int numberOfGuests;
    private ReservationStatus status;
    private Long userId;
    private Long accommodationId;

    public ReservationDto() {
        super();
    }

    public ReservationDto(Long id, Long startDate, Long endDate, int numberOfGuests, ReservationStatus status, Long userId, Long accommodationId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.userId = userId;
        this.accommodationId = accommodationId;
    }

    public ReservationDto(Reservation reservation) {
        this.id = reservation.getId();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.numberOfGuests = reservation.getNumberOfGuests();
        this.status = reservation.getStatus();
        this.userId = reservation.getUser().getId();
        this.accommodationId = reservation.getAccommodation().getId();
    }

    public Long getId() {
        return id;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }


}
