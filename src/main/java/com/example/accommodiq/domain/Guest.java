package com.example.accommodiq.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.Set;

public class Guest extends User {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Accommodation> favorite;

    public Guest() {
        super();
    }

    public Guest(Long id, String firstName, String lastName, String address, String phoneNumber) {
        super(id, firstName, lastName, address, phoneNumber);
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Accommodation> getFavorite() {
        return favorite;
    }

    public void setFavorite(Set<Accommodation> favorite) {
        this.favorite = favorite;
    }
}
