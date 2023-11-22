package com.example.accommodiq.services;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.IReservationService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class ReservationServiceImpl implements IReservationService {

    final
    ReservationRepository allReservations;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    public ReservationServiceImpl(ReservationRepository allReservations) {
        this.allReservations = allReservations;
    }

    @Override
    public Collection<Reservation> getAll() {
        System.out.println(allReservations.findAll());
        return allReservations.findAll();
    }

    @Override
    public Reservation findReservation(Long reservationId) {
        Optional<Reservation> found = allReservations.findById(reservationId);
        if (found.isEmpty()) {
            String value = bundle.getString("reservationNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Reservation insert(Reservation reservation) {
        try {
            allReservations.save(reservation);
            allReservations.flush();
            return reservation;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error: " + ex.getMessage());
        }
    }

    @Override
    public Reservation update(Reservation reservation) {
        try {
            findReservation(reservation.getId()); // this will throw ResponseStatusException if reservation is not found
            allReservations.save(reservation);
            allReservations.flush();
            return reservation;
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data integrity violation");
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
    }

    @Override
    public Reservation delete(Long reservationId) {
        Reservation found = findReservation(reservationId);
        allReservations.delete(found);
        allReservations.flush();
        return found;
    }

    @Override
    public void deleteAll() {
        allReservations.deleteAll();
        allReservations.flush();
    }

    @Override
    public Collection<Reservation> findReservationsByAccommodationId(Long accommodationId) {
        Collection<Reservation> found = allReservations.findByAccommodationId(accommodationId);
        if (found.isEmpty()) {
            String value = bundle.getString("reservationNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found;
    }

    @Override
    public Collection<Reservation> findReservationsByUserId(Long userId) {
        Collection<Reservation> found = allReservations.findByUserId(userId);
        if (found.isEmpty()) {
            String value = bundle.getString("reservationNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found;
    }

    @Override
    public Reservation setReservationStatus(Long reservationId, ReservationStatus status) {
        Optional<Reservation> optionalReservation = allReservations.findById(reservationId);
        if(optionalReservation.isPresent()){
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(status);
            allReservations.save(reservation);
            allReservations.flush();
            return reservation;
        }
        else{
            String value = bundle.getString("reservationNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
    }


}
