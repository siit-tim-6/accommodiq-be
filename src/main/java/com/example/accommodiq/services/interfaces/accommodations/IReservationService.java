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

    Collection<Reservation> findReservationsByAccommodationId(Long accommodationId);

    Collection<Reservation> findReservationsByGuestId(Long userId);

    ReservationCardDto changeReservationStatus(Long reservationId, ReservationStatus status);

    void validateGuestReviewEligibility(Long guestId, Long hostId);

    void deleteByAccommodationId(Long accommodationId);

    Collection<Reservation> getPastReservations(Long ownerId, Long guestId);

    void deleteByGuestId(Long userId);

    List<Reservation> findGuestAcceptedReservationsNotEndedYet(Long userId);

    List<Reservation> findHostReservationsNotEndedYet(Long userId);

    Collection<ReservationCardDto> findHostReservations(Long hostId);

    Collection<HostReservationCardDto> findHostReservationsByFilter(Long hostId, String title, Long startDate, Long endDate, ReservationStatus status);
}
