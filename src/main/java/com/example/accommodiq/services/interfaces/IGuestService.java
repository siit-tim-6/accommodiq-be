package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.*;

import java.util.Collection;

public interface IGuestService {
    Collection<ReservationListDto> getReservations(Long guestId);

    Reservation addReservation(Long guestId, ReservationRequestDto reservationDto);

    Collection<AccommodationListDto> getFavorites(Long guestId);

    AccommodationListDto addFavorite(Long guestId, GuestFavoriteDto favoriteDto);

    MessageDto removeFavorite(Long guestId, Long accommodationId);
}
