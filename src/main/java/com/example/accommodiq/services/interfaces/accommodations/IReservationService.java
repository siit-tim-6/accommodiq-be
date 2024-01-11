package com.example.accommodiq.services.interfaces.accommodations;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.*;


import java.util.Collection;
import java.util.List;

public interface IReservationService {
    Collection<Reservation> getAll();

    Reservation findReservation(Long reservationId);

    ReservationDto findReservationDto(Long reservationId);

    Reservation insert(ReservationRequestDto reservationDto);

    ReservationDto update(ReservationDto reservationDto);

    MessageDto delete(Long reservationId);

    void deleteAll();

    Collection<Reservation> findReservationsByAccommodationId(Long accommodationId);

    Collection<Reservation> findReservationsByGuestId(Long userId);

    ReservationCardDto setReservationStatus(Long reservationId, ReservationStatusDto statusDto);

    void validateGuestReviewEligibility(Long guestId, Long hostId);

    void deleteByAccommodationId(Long accommodationId);

    Collection<Reservation> getPastReservations(Long ownerId, Long guestId);

    void deleteByGuestId(Long userId);

    List<Reservation> findGuestAcceptedReservationsNotEndedYet(Long userId);

    List<Reservation> findHostReservationsNotEndedYet(Long userId);
}
