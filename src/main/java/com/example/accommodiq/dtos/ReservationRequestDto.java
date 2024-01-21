package com.example.accommodiq.dtos;


import com.example.accommodiq.validation.FutureLongDate;
import jakarta.validation.constraints.Min;

public class ReservationRequestDto {
    @FutureLongDate
    private long startDate;
    @FutureLongDate
    private long endDate;
    @Min(value = 1)
    private int numberOfGuests;
    @Min(value = 1)
    private Long accommodationId;

    public ReservationRequestDto() {
        super();
    }

    public ReservationRequestDto(long startDate, long endDate, int numberOfGuests, Long accommodationId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.accommodationId = accommodationId;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }
}
