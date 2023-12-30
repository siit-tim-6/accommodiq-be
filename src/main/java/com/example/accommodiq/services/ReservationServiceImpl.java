package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReservationDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.dtos.ReservationStatusDto;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import com.example.accommodiq.services.interfaces.IReservationService;
import com.example.accommodiq.services.interfaces.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements IReservationService {

    final
    ReservationRepository allReservations;
    final IAccommodationService accommodationService;
    final IUserService userService;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    public ReservationServiceImpl(ReservationRepository allReservations, IAccommodationService accommodationService, IUserService userService) {
        this.allReservations = allReservations;
        this.accommodationService = accommodationService;
        this.userService = userService;
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
    public ReservationDto findReservationDto(Long reservationId) {
        Optional<Reservation> found = allReservations.findById(reservationId);
        if (found.isEmpty()) {
            String value = bundle.getString("reservationNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return new ReservationDto(found.get());
    }

    @Override
    public Reservation insert(ReservationRequestDto reservationDto) {
        Reservation reservation = convertToReservation(reservationDto);
        try {
            allReservations.save(reservation);
            allReservations.flush();
            return reservation;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error: " + ex.getMessage());
        }
    }

    @Override
    public ReservationDto update(ReservationDto reservationDto) {
        Reservation existingReservation = findReservation(reservationDto.getId());

        existingReservation.setStartDate(reservationDto.getStartDate());
        existingReservation.setEndDate(reservationDto.getEndDate());
        existingReservation.setNumberOfGuests(reservationDto.getNumberOfGuests());
        existingReservation.setStatus(reservationDto.getStatus());

        try {
            findReservation(existingReservation.getId()); // this will throw ResponseStatusException if reservation is not found
            allReservations.save(existingReservation);
            allReservations.flush();
            return new ReservationDto(existingReservation);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data integrity violation");
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
    }

    @Override
    public MessageDto delete(Long reservationId) {
        Reservation found = findReservation(reservationId);
        allReservations.delete(found);
        allReservations.flush();
        return new MessageDto("Reservation deleted successfully");
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
    public Reservation setReservationStatus(Long reservationId, ReservationStatusDto statusDto) {
        Optional<Reservation> optionalReservation = allReservations.findById(reservationId);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(statusDto.getStatus());
            allReservations.save(reservation);
            allReservations.flush();
            return reservation;
        } else {
            String value = bundle.getString("reservationNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
    }

    @Override
    public void deleteByAccommodationId(Long accommodationId) {
        allReservations.deleteByAccommodationId(accommodationId);
        allReservations.flush();
    }

    @Override
    public void deleteByUserId(Long userId) {
        allReservations.deleteByUserId(userId);
        allReservations.flush();
    }

    private Reservation convertToReservation(ReservationRequestDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setId(null);
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setNumberOfGuests(reservationDto.getNumberOfGuests());
        reservation.setUser(null);
        reservation.setAccommodation(null);
        return reservation;
    }

    @Override
    public void canGuestCommentAndRateHost(Long guestId, Long hostId) {
        Collection<Accommodation> hostAccommodations = accommodationService.findAccommodationsByHostId(hostId);

        List<Long> accommodationIds = hostAccommodations.stream()
                .map(Accommodation::getId)
                .collect(Collectors.toList());

        long currentTime = System.currentTimeMillis();
        Collection<Reservation> reservations = allReservations
                .findByUserIdAndAccommodationIdInAndStatusNotAndEndDateBefore(guestId, accommodationIds, ReservationStatus.CANCELLED, currentTime);

        if (reservations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Guest cannot comment and rate this host, because he has not stayed in any of his accommodations");
        }
    }
}
