package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.ReservationDto;
import com.example.accommodiq.enums.ReservationStatus;

import java.util.Collection;

public interface IReservationService {
    public Collection<Reservation> getAll();
    public Reservation findReservation(Long reservationId);

    ReservationDto findReservationDto(Long reservationId);

    public Reservation insert(Reservation reservation);

    ReservationDto insert(ReservationDto reservationDto);

    ReservationDto update(ReservationDto reservationDto);

    public Reservation delete(Long reservationId);
    public void deleteAll();

    Collection<Reservation> findReservationsByAccommodationId(Long accommodationId);

    Collection<Reservation> findReservationsByUserId(Long userId);

    Reservation setReservationStatus(Long reservationId, ReservationStatus status);
}
