package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.ReservationListDto;

import java.util.Collection;

public interface IGuestService {
    public Collection<ReservationListDto> getReservations(Long guestId);

    public Collection<AccommodationListDto> getFavorites(Long guestId);
}
