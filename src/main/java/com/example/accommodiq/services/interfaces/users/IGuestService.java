package com.example.accommodiq.services.interfaces.users;

import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.dtos.*;

import java.util.Collection;

public interface IGuestService {
    Guest findGuest(Long guestId);

    Collection<ReservationListDto> getReservations(Long guestId);

    ReservationRequestDto addReservation(Long guestId, ReservationRequestDto reservationDto);

    Collection<AccommodationListDto> getFavorites(Long guestId);

    AccommodationListDto addFavorite(Long guestId, GuestFavoriteDto favoriteDto);

    MessageDto removeFavorite(Long guestId, Long accommodationId);
}
