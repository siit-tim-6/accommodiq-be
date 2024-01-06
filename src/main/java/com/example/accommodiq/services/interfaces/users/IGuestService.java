package com.example.accommodiq.services.interfaces.users;

import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.dtos.*;

import java.util.Collection;

public interface IGuestService {
    Guest findGuest(Long guestId);

    Collection<ReservationListDto> getReservations();

    ReservationRequestDto addReservation(Long guestId, ReservationRequestDto reservationDto);

    Collection<AccommodationCardDto> getFavorites();

    AccommodationCardDto addFavorite(GuestFavoriteDto favoriteDto);

    MessageDto removeFavorite(Long accommodationId);
}
