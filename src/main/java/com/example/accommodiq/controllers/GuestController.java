package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.*;
import com.example.accommodiq.dtos.AccommodationCardDto;
import com.example.accommodiq.dtos.GuestFavoriteDto;
import com.example.accommodiq.dtos.ReservationListDto;
import com.example.accommodiq.services.interfaces.users.IGuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @GetMapping("/{guestId}/favorites")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Get all favorites of guest")
    public Collection<AccommodationCardDto> getFavorites(@Parameter(description = "Id of guest to get favorites") @PathVariable Long guestId) {
        return guestService.getFavorites(guestId);
    }

    @PostMapping("/{guestId}/favorites")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Add favorite to guest")
    public AccommodationCardDto addFavorite(@Parameter(description = "Id of guest to add favorites") @PathVariable Long guestId, @RequestBody GuestFavoriteDto guestFavoriteDto) {
        return guestService.addFavorite(guestId, guestFavoriteDto);
    }

    @DeleteMapping("/{guestId}/favorites/{accommodationId}")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Remove favorite from guest")
    public MessageDto removeFavorite(@Parameter(description = "Id of guest to remove favorites") @PathVariable Long guestId, @Parameter(description = "Id of accommodation to remove from favorites") @PathVariable Long accommodationId) {
        return guestService.removeFavorite(guestId, accommodationId);
    }
}
