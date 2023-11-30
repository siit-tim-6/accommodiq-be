package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.ReservationStatus;

import java.util.Date;

public class ReservationRequestDto {
    private Date startDate;
    private Date endDate;
    private int numberOfGuests;
    private ReservationStatus status;
    private Long userId;
    private Long accommodationId;

    public ReservationRequestDto() {
        super();
    }

    public ReservationRequestDto(Date startDate, Date endDate, int numberOfGuests, ReservationStatus status, Long userId, Long accommodationId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.userId = userId;
        this.accommodationId = accommodationId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }
}
