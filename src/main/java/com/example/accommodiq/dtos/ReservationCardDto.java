package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Location;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.enums.ReservationStatus;

public class ReservationCardDto {
    private long accommodationId;
    private String accommodationImage;
    private String accommodationTitle;
    private double accommodationRating;
    private int accommodationReviewCount;
    private Location accommodationLocation;
    private int guests;
    private long startDate;
    private long endDate;
    private ReservationStatus status;
    private double totalPrice;

    public ReservationCardDto() {
        super();
    }

    public ReservationCardDto(long accommodationId, String accommodationImage, String accommodationTitle, double accommodationRating, int accommodationReviewCount,
                              Location accommodationLocation, int guests, long startDate, long endDate, ReservationStatus status, double totalPrice) {
        this.accommodationId = accommodationId;
        this.accommodationImage = accommodationImage;
        this.accommodationTitle = accommodationTitle;
        this.accommodationRating = accommodationRating;
        this.accommodationReviewCount = accommodationReviewCount;
        this.accommodationLocation = accommodationLocation;
        this.guests = guests;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public ReservationCardDto(Reservation reservation) {
        this.accommodationId = reservation.getAccommodation().getId();
        this.accommodationImage = (!reservation.getAccommodation().getImages().isEmpty()) ? reservation.getAccommodation().getImages().get(0) : "";
        this.accommodationTitle = reservation.getAccommodation().getTitle();
        this.accommodationRating = reservation.getAccommodation().getAverageRating();
        this.accommodationReviewCount = reservation.getAccommodation().getReviews().size();
        this.accommodationLocation = reservation.getAccommodation().getLocation();
        this.guests = reservation.getNumberOfGuests();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.status = reservation.getStatus();
        this.totalPrice = reservation.getAccommodation().getTotalPrice(this.startDate, this.endDate, this.guests);
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

    public double getAccommodationRating() {
        return accommodationRating;
    }

    public void setAccommodationRating(double accommodationRating) {
        this.accommodationRating = accommodationRating;
    }

    public int getAccommodationReviewCount() {
        return accommodationReviewCount;
    }

    public void setAccommodationReviewCount(int accommodationReviewCount) {
        this.accommodationReviewCount = accommodationReviewCount;
    }

    public Location getAccommodationLocation() {
        return accommodationLocation;
    }

    public void setAccommodationLocation(Location accommodationLocation) {
        this.accommodationLocation = accommodationLocation;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
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

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
