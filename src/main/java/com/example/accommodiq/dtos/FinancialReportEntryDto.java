package com.example.accommodiq.dtos;

public class FinancialReportEntryDto {
    private String accommodationTitle;
    private double revenue;
    private int reservationCount;

    public FinancialReportEntryDto() {
        super();
    }

    public FinancialReportEntryDto(String accommodationTitle, double revenue, int reservationCount) {
        this.accommodationTitle = accommodationTitle;
        this.revenue = revenue;
        this.reservationCount = reservationCount;
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
