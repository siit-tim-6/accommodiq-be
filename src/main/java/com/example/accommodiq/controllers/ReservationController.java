package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.services.interfaces.IReservationService;
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

    public ReservationController(IReservationService reservationService) {
        this.reservationService = reservationService;
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
    public ResponseEntity<String> deleteReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.findReservation(reservationId);
        reservationService.delete(reservationId);
        return ResponseEntity.ok("Reservation with ID " + reservationId + " has been deleted.");
    }

    //@DeleteMapping("/{reservationId}")
    //public Reservation delete(@PathVariable Long reservationId) { return reservationService.delete(reservationId); }

    @DeleteMapping
    public void deleteAll() { reservationService.deleteAll(); }

    @PutMapping("/{reservationId}/accept")
    public ResponseEntity<String> acceptReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.findReservation(reservationId);
        reservation.setStatus(Reservation.Status.ACCEPTED);
        reservationService.update(reservation);
        return ResponseEntity.ok("Reservation with ID " + reservationId + " has been accepted.");
    }

    @PutMapping("/{reservationId}/deny")
    public ResponseEntity<String> denyReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.findReservation(reservationId);
        reservation.setStatus(Reservation.Status.DECLINED);
        reservationService.update(reservation);
        return ResponseEntity.ok("Reservation with ID " + reservationId + " has been denied.");
    }

    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.findReservation(reservationId);
        reservation.setStatus(Reservation.Status.CANCELLED);
        reservationService.update(reservation);
        return ResponseEntity.ok("Reservation with ID " + reservationId + " has been cancelled.");
    }

}
