package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Location;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.enums.ReservationStatus;

public class HostReservationCardDto extends ReservationCardDto {
    private String guestName;
    private int pastCancellations;

    public HostReservationCardDto() {
        super();
    }

    public HostReservationCardDto(long id, long accommodationId, String accommodationImage, String accommodationTitle, double accommodationRating, int accommodationReviewCount, Location accommodationLocation, int guests, long startDate, long endDate, ReservationStatus status, double totalPrice, String guestName, int pastCancellations) {
        super(id, accommodationId, accommodationImage, accommodationTitle, accommodationRating, accommodationReviewCount, accommodationLocation, guests, startDate, endDate, status, totalPrice);
        this.guestName = guestName;
        this.pastCancellations = pastCancellations;
    }

    public HostReservationCardDto(Reservation reservation, long hostId) {
        super(reservation);
        this.guestName = reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName();
        this.pastCancellations = reservation.getGuest().getPreviousCancellationNumber(hostId);
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public int getPastCancellations() {
        return pastCancellations;
    }

    public void setPastCancellations(int pastCancellations) {
        this.pastCancellations = pastCancellations;
    }
}
