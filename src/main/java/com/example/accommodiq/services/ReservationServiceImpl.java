package com.example.accommodiq.services;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.IReservationService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class ReservationServiceImpl implements IReservationService {

    @Autowired
    ReservationRepository allReservations;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
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
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "error");
        }
    }

    @Override
    public Reservation update(Reservation reservation) {
        try {
            findReservation(reservation.getId()); // this will throw ResponseStatusException if reservation is not found
            allReservations.save(reservation);
            allReservations.flush();
            return reservation;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while((e != null) && !((c=e.getCause()) instanceof ConstraintViolationException))
                e = (RuntimeException)c;
            if ((c!= null) && (c instanceof ConstraintViolationException))
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "error");
            throw ex;
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
}
