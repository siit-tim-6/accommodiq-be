package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.ReservationStatusDto;
import com.example.accommodiq.enums.ReservationStatus;

import java.util.Collection;

public interface IReservationService {
    public Collection<Reservation> getAll();

    public Reservation findReservation(Long reservationId);

    public Reservation insert(Reservation reservation);

    public Reservation update(Reservation reservation);

    public Reservation delete(Long reservationId);

    public void deleteAll();

    Collection<Reservation> findReservationsByAccommodationId(Long accommodationId);

    Collection<Reservation> findReservationsByUserId(Long userId);

    Reservation setReservationStatus(Long reservationId, ReservationStatusDto statusDto);
}
