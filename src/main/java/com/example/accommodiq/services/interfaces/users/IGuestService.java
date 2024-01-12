package com.example.accommodiq.services.interfaces.users;

import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.ReservationStatus;

import java.util.Collection;

public interface IGuestService {
    Guest findGuest(Long guestId);

    Collection<ReservationCardDto> getReservations();

    Collection<ReservationCardDto> findReservationsByFilter(String title, Long startDate, Long endDate, ReservationStatus status);

    ReservationRequestDto addReservation(Long guestId, ReservationRequestDto reservationDto);

    Collection<AccommodationCardDto> getFavorites();

    AccommodationCardDto addFavorite(GuestFavoriteDto favoriteDto);

    MessageDto removeFavorite(Long accommodationId);

    Collection<Long> getCancellableReservationIds();
}
