package com.example.accommodiq.services.interfaces.accommodations;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReservationDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.dtos.ReservationStatusDto;


import java.util.Collection;

public interface IReservationService {
    Collection<Reservation> getAll();

    Reservation findReservation(Long reservationId);

    ReservationDto findReservationDto(Long reservationId);

    Reservation insert(ReservationRequestDto reservationDto);

    ReservationDto update(ReservationDto reservationDto);

    MessageDto delete(Long reservationId);

    void deleteAll();

    Collection<Reservation> findReservationsByAccommodationId(Long accommodationId);

    Collection<Reservation> findReservationsByUserId(Long userId);

    Reservation setReservationStatus(Long reservationId, ReservationStatusDto statusDto);

    void canGuestCommentAndRateHost(Long guestId, Long hostId);

    void deleteByAccommodationId(Long accommodationId);

    void deleteByUserId(Long userId);

    void canGuestCommentAndRateAccommodation(Long guestId, Long accommodationId);
}
