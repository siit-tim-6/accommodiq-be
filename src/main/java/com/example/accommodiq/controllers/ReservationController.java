package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Reservation;

import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReservationDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.dtos.ReservationStatusDto;
import com.example.accommodiq.services.interfaces.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    IReservationService reservationService;

    @Autowired
    public ReservationController(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Reservation> getReservations() {
        return reservationService.getAll();
    }

    @GetMapping("/{reservationId}")
    public ReservationDto findReservationById(@PathVariable Long reservationId) {
        return reservationService.findReservationDto(reservationId);
    }

    @PostMapping
    public Reservation insert(@RequestBody ReservationRequestDto reservationDto) {
        return reservationService.insert(reservationDto);
    }

    @PutMapping
    public ReservationDto update(@RequestBody ReservationDto reservationDto) {
        return reservationService.update(reservationDto);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAll() {
        reservationService.deleteAll();
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('GUEST')")
    public MessageDto deleteReservation(@PathVariable Long reservationId) {
        return reservationService.delete(reservationId);
    }

    @PutMapping("/{reservationId}/status")
    @PreAuthorize("hasAuthority('HOST')")
    public Reservation acceptReservation(@PathVariable Long reservationId, @RequestBody ReservationStatusDto body) {
        return reservationService.setReservationStatus(reservationId, body);
    }
}
