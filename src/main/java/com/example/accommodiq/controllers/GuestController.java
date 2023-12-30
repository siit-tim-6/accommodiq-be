package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.users.IGuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Get all reservations of guest")
    public Collection<ReservationListDto> getReservations(@PathVariable Long guestId) {
        return guestService.getReservations(guestId);
    }

    @PostMapping("/{guestId}/reservations")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Add reservation to guest")
    public ReservationRequestDto addReservation(@Parameter(description = "Id of guest to add reservation") @PathVariable Long guestId, @RequestBody ReservationRequestDto reservationDto) {
        return guestService.addReservation(guestId, reservationDto);
    }

    @GetMapping("/favorites")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Get all favorites of guest")
    public Collection<AccommodationCardDto> getFavorites() {
        return guestService.getFavorites();
    }

    @PostMapping("/favorites")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Add favorite to guest")
    public AccommodationCardDto addFavorite(@RequestBody GuestFavoriteDto guestFavoriteDto) {
        return guestService.addFavorite(guestFavoriteDto);
    }

    @DeleteMapping("/favorites/{accommodationId}")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Remove favorite from guest")
    public ResponseEntity<String> removeFavorite(@Parameter(description = "Id of accommodation to remove from favorites") @PathVariable Long accommodationId) {
        return guestService.removeFavorite(accommodationId);
    }

}
