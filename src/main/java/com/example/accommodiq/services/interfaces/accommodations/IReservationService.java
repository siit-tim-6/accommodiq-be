package com.example.accommodiq.services.interfaces.accommodations;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.ReservationStatus;

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

    ReservationCardDto changeReservationStatus(Long reservationId, ReservationStatus status);

    void validateGuestReviewEligibility(Long guestId, Long hostId);

    Collection<Reservation> getPastReservations(Long ownerId, Long guestId);

    void deleteByGuestId(Long userId);

    List<Reservation> findGuestAcceptedReservationsNotEndedYet(Long userId);

    List<Reservation> findHostReservationsNotEndedYet(Long userId);

    Collection<Reservation> findHostReservationsByFilter(Long hostId, String title, Long startDate, Long endDate, ReservationStatus status);

    void cancelGuestReservations(Long id);
}
