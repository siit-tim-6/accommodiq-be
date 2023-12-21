package com.example.accommodiq.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Set;

@Entity
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

    public Guest(User user) {
        super(user.getId(), user.getFirstName(), user.getLastName(), user.getAddress(), user.getPhoneNumber());
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

    public boolean canCreateReservation(long startDate, long endDate, long accommodationId) {
        return reservations.stream().noneMatch((reservation -> reservation.getAccommodation().getId() == accommodationId
                && (reservation.getStartDate() <= startDate && startDate <= reservation.getEndDate()
                || reservation.getStartDate() <= endDate && endDate <= reservation.getEndDate())));
    }
}
