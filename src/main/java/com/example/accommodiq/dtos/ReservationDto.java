package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.ReservationStatus;

import java.util.Date;

public class ReservationDto {
    private Long id;
    private Date startDate;
    private Date endDate;
    private int numberOfGuests;
    private ReservationStatus status;
    private Long userId;
    private Long accommodationId;

    public ReservationDto() {
    }

    public ReservationDto(Long id, Date startDate, Date endDate, int numberOfGuests, ReservationStatus status, Long userId, Long accommodationId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.userId = userId;
        this.accommodationId = accommodationId;
    }

    public Long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
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
