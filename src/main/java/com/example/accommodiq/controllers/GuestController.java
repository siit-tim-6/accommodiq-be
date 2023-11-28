package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.GuestFavoriteDto;
import com.example.accommodiq.dtos.ReservationListDto;
import com.example.accommodiq.services.interfaces.IGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/guests")
public class GuestController {
    final IGuestService guestService;

    @Autowired
    public GuestController(IGuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/{guestId}/reservations")
    public Collection<ReservationListDto> getReservations(@PathVariable Long guestId) {
        return guestService.getReservations(guestId);
    }

    @PostMapping("/{guestId}/reservations")
    public Reservation addReservation(@PathVariable Long guestId, @RequestBody Reservation reservation) {
        return reservation;
    }

    @GetMapping("/{guestId}/favorites")
    public Collection<AccommodationListDto> getFavorites(@PathVariable Long guestId) {
        return guestService.getFavorites(guestId);
    }

    @PostMapping("/{guestId}/favorites")
    public GuestFavoriteDto addFavorite(@PathVariable Long guestId, @RequestBody GuestFavoriteDto guestFavoriteDto) {
        return guestFavoriteDto;
    }

    @DeleteMapping("/{guestId}/favorites/{accommodationId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFavorite(@PathVariable Long guestId, @PathVariable Long accommodationId) {

    }
}
