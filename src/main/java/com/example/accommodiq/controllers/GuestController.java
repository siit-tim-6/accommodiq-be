package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.GuestFavoriteDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/guests")
public class GuestController {
    @GetMapping("/{guestId}/reservations")
    public Collection<Reservation> getReservations(@PathVariable Long guestId) {
        return null;
    }

    @PostMapping("/{guestId}/reservations")
    public Reservation addReservation(@PathVariable Long guestId, @RequestBody Reservation reservation) {
        return reservation;
    }

    @GetMapping("/{guestId}/favorites")
    public Collection<Accommodation> getFavorites(@PathVariable Long guestId) {
        return null;
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
