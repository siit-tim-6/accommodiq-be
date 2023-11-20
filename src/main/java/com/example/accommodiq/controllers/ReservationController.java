package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.services.interfaces.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    IReservationService service;

    @GetMapping
    public Collection<Reservation> getReservations() { return service.getAll();}

    @GetMapping("/{reservationId}")
    public Reservation findReservationById(@PathVariable Long reservationId) { return service.findReservation(reservationId); }

    @PostMapping
    public Reservation insert(@RequestBody Reservation reservation) { return service.insert(reservation); }

    @PutMapping
    public Reservation update(@RequestBody Reservation reservation) { return service.update(reservation); }

    @DeleteMapping("/{reservationId}")
    public Reservation delete(@PathVariable Long reservationId) { return service.delete(reservationId); }

    @DeleteMapping
    public void deleteAll() { service.deleteAll(); }
}
