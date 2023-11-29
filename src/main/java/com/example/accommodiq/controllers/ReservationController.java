package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Reservation;

import com.example.accommodiq.dtos.ReservationDto;
import com.example.accommodiq.dtos.ReservationStatusDto;
import com.example.accommodiq.services.interfaces.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public Collection<Reservation> getReservations() {
        return reservationService.getAll();
    }

    @GetMapping("/{reservationId}")
    public ReservationDto findReservationById(@PathVariable Long reservationId) {
        return reservationService.findReservationDto(reservationId);
    }

    @PostMapping
    public ReservationDto insert(@RequestBody ReservationDto reservationDto) {
        return reservationService.insert(reservationDto);
    }

    @PutMapping
    public ReservationDto update(@RequestBody ReservationDto reservationDto) {
        return reservationService.update(reservationDto);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long reservationId) {
        reservationService.delete(reservationId);
        return ResponseEntity.ok("Reservation with ID " + reservationId + " has been deleted.");
    }

    @DeleteMapping
    public void deleteAll() {
        reservationService.deleteAll();
    }

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<String> acceptReservation(@PathVariable Long reservationId, @RequestBody ReservationStatusDto body) {
        reservationService.setReservationStatus(reservationId, body);
        return ResponseEntity.ok("Reservation with ID " + reservationId + " status changed.");
    }
}
