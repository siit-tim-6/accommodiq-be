package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class FinancialReportEntryDto {
    private long accommodationId;
    private String accommodationImage;
    private String accommodationTitle;
    private double revenue;
    private int reservationCount;

    public FinancialReportEntryDto() {
        super();
    }

    public FinancialReportEntryDto(long accommodationId, String accommodationImage, String accommodationTitle, double revenue, int reservationCount) {
        this.accommodationId = accommodationId;
        this.accommodationImage = accommodationImage;
        this.accommodationTitle = accommodationTitle;
        this.revenue = revenue;
        this.reservationCount = reservationCount;
    }

    public FinancialReportEntryDto(List<Reservation> reservations) {
        this.accommodationId = reservations.get(0).getAccommodation().getId();
        this.accommodationImage = (!reservations.get(0).getAccommodation().getImages().isEmpty()) ? reservations.get(0).getAccommodation().getImages().get(0) : "";
        this.accommodationTitle = reservations.get(0).getAccommodation().getTitle();
        this.revenue = reservations.stream().mapToDouble(Reservation::getTotalPrice).sum();
        this.reservationCount = reservations.size();
    }

    public long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getAccommodationImage() {
        return accommodationImage;
    }

    public void setAccommodationImage(String accommodationImage) {
        this.accommodationImage = accommodationImage;
    }

    public String getAccommodationTitle() {
        return accommodationTitle;
    }

    public void setAccommodationTitle(String accommodationTitle) {
        this.accommodationTitle = accommodationTitle;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }
}
