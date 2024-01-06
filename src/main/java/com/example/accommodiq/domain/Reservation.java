package com.example.accommodiq.domain;

import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.enums.ReservationStatus;
import jakarta.persistence.*;


@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long startDate;
    private Long endDate;
    private int numberOfGuests;
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Guest guest;
    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    public Reservation() {
    }

    public Reservation(Long id, Long startDate, Long endDate, int numberOfGuests, ReservationStatus status, Guest guest, Accommodation accommodation) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.guest = guest;
        this.accommodation = accommodation;
    }

    public Reservation(ReservationRequestDto reservationRequestDto, Guest guest, Accommodation accommodation) {
        this.startDate = reservationRequestDto.getStartDate();
        this.endDate = reservationRequestDto.getEndDate();
        this.numberOfGuests = reservationRequestDto.getNumberOfGuests();
        this.status = ReservationStatus.PENDING;
        this.guest = guest;
        this.accommodation = accommodation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
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

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }
}
