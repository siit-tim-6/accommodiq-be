package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Reservation;

import java.util.Collection;
import java.util.List;

public class FinancialReportMonthlyRevenueDto {
    private String month;
    private double revenue;
    private int reservationCount;

    public FinancialReportMonthlyRevenueDto() {
        super();
    }

    public FinancialReportMonthlyRevenueDto(String month, double revenue, int reservationCount) {
        this.month = month;
        this.revenue = revenue;
        this.reservationCount = reservationCount;
    }

    public FinancialReportMonthlyRevenueDto(String month, Collection<Reservation> reservations) {
        this.month = month;
        this.revenue = reservations.stream().mapToDouble(Reservation::getTotalPrice).sum();
        this.reservationCount = reservations.size();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
