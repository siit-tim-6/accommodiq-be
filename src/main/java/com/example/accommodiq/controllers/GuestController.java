package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.GuestFavoriteDto;
import com.example.accommodiq.dtos.ReservationDto;
import com.example.accommodiq.dtos.ReservationListDto;
import com.example.accommodiq.services.interfaces.IGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping("/guests")
public class GuestController {
    final IGuestService guestService;

    @Autowired
    public GuestController(IGuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/{guestId}/reservations")
    @PreAuthorize("hasAuthority('GUEST')")
    public Collection<ReservationListDto> getReservations(@PathVariable Long guestId) {
        return guestService.getReservations(guestId);
    }

    @PostMapping("/{guestId}/reservations")
    public ReservationRequestDto addReservation(@PathVariable Long guestId, @RequestBody ReservationRequestDto reservationDto) {
        return guestService.addReservation(guestId, reservationDto);
    }

    @GetMapping("/{guestId}/favorites")
    public Collection<AccommodationListDto> getFavorites(@PathVariable Long guestId) {
        return guestService.getFavorites(guestId);
    }

    @PostMapping("/{guestId}/favorites")
    public AccommodationListDto addFavorite(@PathVariable Long guestId, @RequestBody GuestFavoriteDto guestFavoriteDto) {
        return guestService.addFavorite(guestId, guestFavoriteDto);
    }

    @DeleteMapping("/{guestId}/favorites/{accommodationId}")
    public MessageDto removeFavorite(@PathVariable Long guestId, @PathVariable Long accommodationId) {
        return guestService.removeFavorite(guestId, accommodationId);
    }
}
