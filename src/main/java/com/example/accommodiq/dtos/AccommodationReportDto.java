package com.example.accommodiq.dtos;

import java.util.ArrayList;

public class AccommodationReportDto {
    private int reservationCount;
    private ArrayList<AccommodationReportRevenueDto> revenues;

    public AccommodationReportDto() {
        super();
    }

    public AccommodationReportDto(int reservationCount, ArrayList<AccommodationReportRevenueDto> revenues) {
        this.reservationCount = reservationCount;
        this.revenues = revenues;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }

    public ArrayList<AccommodationReportRevenueDto> getRevenues() {
        return revenues;
    }

    public void setRevenues(ArrayList<AccommodationReportRevenueDto> revenues) {
        this.revenues = revenues;
    }
}
