package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Reservation;

import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.accommodations.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping("/reservations")
public class ReservationController {
    IReservationService reservationService;

    @Autowired
    public ReservationController(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all reservations")
    public Collection<Reservation> getReservations() {
        return reservationService.getAll();
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "Get reservation by id")
    public ReservationDto findReservationById(@Parameter(description = "Id of reservation to get data") @PathVariable Long reservationId) {
        return reservationService.findReservationDto(reservationId);
    }

    @PostMapping
    @Operation(summary = "Create reservation")
    public Reservation insert(@RequestBody ReservationRequestDto reservationDto) {
        return reservationService.insert(reservationDto);
    }

    @PutMapping
    @Operation(summary = "Update reservation")
    public ReservationDto update(@Parameter(description = "Updated reservation") @RequestBody ReservationDto reservationDto) {
        return reservationService.update(reservationDto);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete all reservations")
    public void deleteAll() {
        reservationService.deleteAll();
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Delete reservation")
    public MessageDto deleteReservation(@Parameter(description = "Id of reservation to be deleted") @PathVariable Long reservationId) {
        return reservationService.delete(reservationId);
    }

    @PutMapping("/{reservationId}/status")
    @PreAuthorize("hasAuthority('HOST') || hasAuthority('GUEST')")
    @Operation(summary = "Change reservation status")
    public ReservationCardDto changeReservationStatus(@Parameter(description = "Id of reservation to change status") @PathVariable Long reservationId, @RequestBody ReservationStatusDto body) {
        return reservationService.setReservationStatus(reservationId, body);
    }
}
