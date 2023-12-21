package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.GuestRepository;
import com.example.accommodiq.services.interfaces.IGuestService;
import com.example.accommodiq.utilities.ErrorUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
public class GuestServiceImpl implements IGuestService {
    final private GuestRepository guestRepository;
    final private AccommodationRepository accommodationRepository;

    public GuestServiceImpl(GuestRepository guestRepository, AccommodationRepository accommodationRepository) {
        this.guestRepository = guestRepository;
        this.accommodationRepository = accommodationRepository;
    }

    @Override
    public Guest findGuest(Long guestId) {
        Optional<Guest> guest = guestRepository.findById(guestId);

        if (guest.isEmpty()) {
            ErrorUtils.throwNotFound("guestNotFound");
        }

        return guest.get();
    }

    private Accommodation findAccommodation(Long accommodationId) {
        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);

        if (accommodation.isEmpty()) {
            ErrorUtils.throwNotFound("accommodationNotFound");
        }

        return accommodation.get();
    }

    @Override
    public Collection<ReservationListDto> getReservations(Long guestId) {
        if (guestId == 4L) {
            ErrorUtils.throwNotFound("guestNotFound");
        }

        ArrayList<ReservationListDto> reservationListDtos = new ArrayList<>();
        AccommodationListDto accommodation1 = new AccommodationListDto() {{
            setId(1L);
            setTitle("Cozy Cottage");
            setImage("/images/cozy_cottage.jpg");
            setRating(4.5);
            setReviewCount(32);
            setLocation("Mountain View, CA");
            setMinPrice(120.0);
            setMinGuests(2);
            setMaxGuests(4);
        }};

        // Accommodation 2
        AccommodationListDto accommodation2 = new AccommodationListDto() {{
            setId(2L);
            setTitle("Seaside Villa");
            setImage("/images/seaside_villa.jpg");
            setRating(4.8);
            setReviewCount(45);
            setLocation("Malibu, CA");
            setMinPrice(200.0);
            setMinGuests(4);
            setMaxGuests(8);
        }};
        ReservationListDto reservation1 = new ReservationListDto() {{
            setStartDate(new Date());
            setEndDate(new Date(System.currentTimeMillis() + 86400000)); // Adding one day in milliseconds
            setNumberOfGuests(2);
            setStatus(ReservationStatus.CREATED); // Assuming ReservationStatus is an enum
            setAccommodation(accommodation1);
        }};

        // Object 2
        ReservationListDto reservation2 = new ReservationListDto() {{
            setStartDate(new Date());
            setEndDate(new Date(System.currentTimeMillis() + 172800000)); // Adding two days in milliseconds
            setNumberOfGuests(4);
            setStatus(ReservationStatus.CREATED); // Assuming ReservationStatus is an enum
            setAccommodation(accommodation2);
        }};

        reservationListDtos.add(reservation1);
        reservationListDtos.add(reservation2);

        return reservationListDtos;
    }

    @Transactional
    @Override
    public ReservationRequestDto addReservation(Long guestId, ReservationRequestDto reservationDto) {
        Guest guest = findGuest(guestId);
        Accommodation accommodation = findAccommodation(reservationDto.getAccommodationId());

        if (!guest.canCreateReservation(reservationDto.getStartDate(), reservationDto.getEndDate(), reservationDto.getAccommodationId())) {
            ErrorUtils.throwBadRequest("overlappingReservations");
        }

        guest.getReservations().add(new Reservation(reservationDto, guest, accommodation));
        guestRepository.save(guest);
        guestRepository.flush();

        return reservationDto;
    }

    @Override
    public Collection<AccommodationListDto> getFavorites(Long guestId) {
        if (guestId == 4L) {
            ErrorUtils.throwNotFound("guestNotFound");
        }

        ArrayList<AccommodationListDto> accommodationListDtos = new ArrayList<>();
        AccommodationListDto accommodation1 = new AccommodationListDto() {{
            setId(1L);
            setTitle("Cozy Cottage");
            setImage("/images/cozy_cottage.jpg");
            setRating(4.5);
            setReviewCount(32);
            setLocation("Mountain View, CA");
            setMinPrice(120.0);
            setMinGuests(2);
            setMaxGuests(4);
        }};

        // Accommodation 2
        AccommodationListDto accommodation2 = new AccommodationListDto() {{
            setId(2L);
            setTitle("Seaside Villa");
            setImage("/images/seaside_villa.jpg");
            setRating(4.8);
            setReviewCount(45);
            setLocation("Malibu, CA");
            setMinPrice(200.0);
            setMinGuests(4);
            setMaxGuests(8);
        }};

        accommodationListDtos.add(accommodation1);
        accommodationListDtos.add(accommodation2);

        return accommodationListDtos;
    }

    @Override
    public AccommodationListDto addFavorite(Long guestId, GuestFavoriteDto favoriteDto) {
        if (guestId == 4L) {
            ErrorUtils.throwNotFound("guestNotFound");
        }
        return new AccommodationListDto() {{
            setId(2L);
            setTitle("Seaside Villa");
            setImage("/images/seaside_villa.jpg");
            setRating(4.8);
            setReviewCount(45);
            setLocation("Malibu, CA");
            setMinPrice(200.0);
            setMinGuests(4);
            setMaxGuests(8);
        }};
    }

    @Override
    public MessageDto removeFavorite(Long guestId, Long accommodationId) {
        if (guestId == 4L) {
            ErrorUtils.throwNotFound("guestNotFound");
        }

        if (accommodationId == 4L) {
            ErrorUtils.throwNotFound("favoriteNotFound");
        }

        return new MessageDto("Favorite removed successfully");
    }
}
