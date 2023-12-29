package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.ReservationStatus;

import java.util.Date;

public class ReservationListDto {
    private Date startDate;
    private Date endDate;
    private int numberOfGuests;
    private ReservationStatus status;
    private AccommodationCardDto accommodation;

    public ReservationListDto() {
        super();
    }

    public ReservationListDto(Date startDate, Date endDate, int numberOfGuests, ReservationStatus status, AccommodationCardDto accommodation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.accommodation = accommodation;
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

    public AccommodationCardDto getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(AccommodationCardDto accommodation) {
        this.accommodation = accommodation;
    }
}
