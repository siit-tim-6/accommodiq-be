package com.example.accommodiq.services.impl.accommodations;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReservationDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.dtos.ReservationStatusDto;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.repositories.ReviewRepository;
import com.example.accommodiq.services.interfaces.accommodations.IAccommodationService;
import com.example.accommodiq.services.interfaces.accommodations.IReservationService;
import com.example.accommodiq.services.interfaces.users.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class ReservationServiceImpl implements IReservationService {

    final
    ReservationRepository allReservations;
    final AccommodationRepository accommodationRepository;
    final IUserService userService;
    final ReviewRepository reviewRepository;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    public ReservationServiceImpl(ReservationRepository allReservations, AccommodationRepository accommodationRepository, IUserService userService, ReviewRepository reviewRepository) {
        this.allReservations = allReservations;
        this.accommodationRepository = accommodationRepository;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
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
    public Collection<Reservation> findReservationsByGuestId(Long guestId) {
        Collection<Reservation> found = allReservations.findByGuestId(guestId);
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
    public void deleteByGuestId(Long guestId) {
        allReservations.deleteByGuestId(guestId);
        allReservations.flush();
    }

    @Override
    public List<Reservation> findGuestAcceptedReservationsNotEndedYet(Long guestId) {
        return allReservations.findByStatusAndGuestIdAndEndDateGreaterThanOrderByStartDateDesc(ReservationStatus.ACCEPTED, guestId, Instant.now().toEpochMilli());
    }

    @Override
    public List<Reservation> findHostReservationsNotEndedYet(Long guestId) {
        return allReservations.findByStatusAndAccommodation_HostIdAndEndDateGreaterThanOrderByStartDateDesc(ReservationStatus.ACCEPTED, guestId, Instant.now().toEpochMilli());
    }

    private Reservation convertToReservation(ReservationRequestDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setId(null);
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setNumberOfGuests(reservationDto.getNumberOfGuests());
        reservation.setGuest(null);
        reservation.setAccommodation(null);
        return reservation;
    }

    @Override
    public void validateGuestReviewEligibility(Long guestId, Long hostId) {
        Set<Review> reviewsForHostByGuest = reviewRepository.findReviewsByGuestIdAndHostId(guestId, hostId);

        List<Long> accommodationIds = accommodationRepository.findByHostId(hostId).stream()
                .map(Accommodation::getId)
                .collect(Collectors.toList());

        long currentTime = System.currentTimeMillis();

        Collection<Reservation> reservations = allReservations
                .findByUserIdAndAccommodationIdInAndStatusNotAndEndDateLessThan(guestId, accommodationIds, ReservationStatus.CANCELLED, currentTime);

        if (reservations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Guest cannot comment and rate this host, because he has not stayed in any of his accommodations");
        }

        if (reviewsForHostByGuest.size() >= reservations.size()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Guest cannot comment and rate this host, as they have already left reviews for all their reservations.");
        }
    }
}
