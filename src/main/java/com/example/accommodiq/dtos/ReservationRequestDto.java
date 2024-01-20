package com.example.accommodiq.dtos;


import com.example.accommodiq.validation.FutureLongDate;
import jakarta.validation.constraints.Min;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationRequestDto that = (ReservationRequestDto) o;
        return startDate == that.startDate && endDate == that.endDate && numberOfGuests == that.numberOfGuests && Objects.equals(accommodationId, that.accommodationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, numberOfGuests, accommodationId);
    }
}
