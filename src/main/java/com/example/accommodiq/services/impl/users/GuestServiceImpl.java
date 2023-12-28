package com.example.accommodiq.services.impl.users;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.GuestRepository;
import com.example.accommodiq.services.interfaces.users.IGuestService;
import com.example.accommodiq.utilities.ErrorUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
            throw ErrorUtils.generateNotFound("guestNotFound");
        }

        return guest.get();
    }

    private Accommodation findAccommodation(Long accommodationId) {
        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);

        if (accommodation.isEmpty()) {
            throw ErrorUtils.generateNotFound("accommodationNotFound");
        }

        return accommodation.get();
    }

    @Override
    public Collection<ReservationListDto> getReservations(Long guestId) { // mocked
        if (guestId == 4L) {
            throw ErrorUtils.generateNotFound("guestNotFound");
        }

        return new ArrayList<>();
    }

    @Transactional
    @Override
    public ReservationRequestDto addReservation(Long guestId, ReservationRequestDto reservationDto) {
        Guest guest = findGuest(guestId);
        Accommodation accommodation = findAccommodation(reservationDto.getAccommodationId());

        if (!guest.canCreateReservation(reservationDto.getStartDate(), reservationDto.getEndDate(), reservationDto.getAccommodationId())) {
            throw ErrorUtils.generateBadRequest("overlappingReservations");
        }

        guest.getReservations().add(new Reservation(reservationDto, guest, accommodation));
        guestRepository.save(guest);
        guestRepository.flush();

        return reservationDto;
    }

    @Override
    public Collection<AccommodationListDto> getFavorites(Long guestId) { // mocked
        if (guestId == 4L) {
            throw ErrorUtils.generateNotFound("guestNotFound");
        }

        return new ArrayList<>();
    }

    @Override
    public AccommodationListDto addFavorite(Long guestId, GuestFavoriteDto favoriteDto) { // mocked
        if (guestId == 4L) {
            throw ErrorUtils.generateNotFound("guestNotFound");
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
    public MessageDto removeFavorite(Long guestId, Long accommodationId) { // mocked
        if (guestId == 4L) {
            throw ErrorUtils.generateNotFound("guestNotFound");
        }

        if (accommodationId == 4L) {
            throw ErrorUtils.generateNotFound("favoriteNotFound");
        }

        return new MessageDto("Favorite removed successfully");
    }
}
