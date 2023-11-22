package com.example.accommodiq.domain;

import com.example.accommodiq.enums.ReservationStatus;
import jakarta.persistence.*;

import java.util.Date;


@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date startDate;
    private Date endDate;
    private int numberOfGuests;
    private ReservationStatus status;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private User user;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Accommodation accommodation;

    public Reservation() {
    }

    public Reservation(Long id, Date startDate, Date endDate, int numberOfGuests, ReservationStatus status, User user, Accommodation accommodation) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.user = user;
        this.accommodation = accommodation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setApartment(Accommodation apartment) {
        this.accommodation = accommodation;
    }

    public Accommodation getApartment() {
        return accommodation;
    }
}
