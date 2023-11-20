package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.services.interfaces.IApartmentService;
import com.example.accommodiq.services.interfaces.IReservationService;
import com.example.accommodiq.services.interfaces.IUserService;
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

    final
    IReservationService reservationService;

    final
    IUserService userService;

    final
    IApartmentService apartmentService;

    public ReservationController(IReservationService reservationService, IUserService userService, IApartmentService apartmentService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.apartmentService = apartmentService;
    }



    @GetMapping
    public Collection<Reservation> getReservations() { return reservationService.getAll();}

    @GetMapping("/{reservationId}")
    public Reservation findReservationById(@PathVariable Long reservationId) { return reservationService.findReservation(reservationId); }

    @PostMapping
    public Reservation insert(@RequestBody Reservation reservation) { return reservationService.insert(reservation); }

    @PutMapping
    public Reservation update(@RequestBody Reservation reservation) { return reservationService.update(reservation); }

    @DeleteMapping("/{reservationId}")
    public Reservation delete(@PathVariable Long reservationId) { return reservationService.delete(reservationId); }

    @DeleteMapping
    public void deleteAll() { reservationService.deleteAll(); }

    @PutMapping("/{reservationId}/accept")
    public ResponseEntity<String> acceptReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.findReservation(reservationId);
        reservation.setStatus(Reservation.Status.ACCEPTED);
        reservationService.update(reservation);
        return ResponseEntity.ok("Reservation with ID " + reservationId + " has been deleted.");
    }

    @PutMapping("/{reservationId}/deny")

}
