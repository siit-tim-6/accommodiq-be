package com.example.accommodiq.services.impl.users;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.GuestRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.users.IGuestService;
import com.example.accommodiq.utilities.ErrorUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class GuestServiceImpl implements IGuestService {
    final private GuestRepository guestRepository;
    final private AccommodationRepository accommodationRepository;
    final private IAccountService accountService;
    final private ReservationRepository reservationRepository;

    @Autowired
    public GuestServiceImpl(GuestRepository guestRepository, AccommodationRepository accommodationRepository, IAccountService accountService, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.accommodationRepository = accommodationRepository;
        this.accountService = accountService;
        this.reservationRepository = reservationRepository;
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
    public Collection<ReservationListDto> getReservations() {
        Long userId = getGuestId();

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
    public Collection<AccommodationCardDto> getFavorites() {
        Long guestId = getGuestId();
        Guest guest = findGuest(guestId);

        return guest.getFavorite().stream().map(AccommodationCardDto::new).toList();
    }

    @Override
    public AccommodationCardDto addFavorite(GuestFavoriteDto favoriteDto) {
        Long guestId = getGuestId();
        Guest guest = findGuest(guestId);
        Accommodation accommodation = findAccommodation(favoriteDto.getFavoriteId());
        if (guest.getFavorite().stream().anyMatch(accommodation1 -> Objects.equals(accommodation1.getId(), accommodation.getId()))) {
            throw ErrorUtils.generateBadRequest("alreadyFavorite");
        }
        guest.getFavorite().add(accommodation);
        guestRepository.save(guest);
        guestRepository.flush();
        return new AccommodationCardDto(accommodation);
    }

    @Override
    public MessageDto removeFavorite(Long accommodationId) {
        Long guestId = getGuestId();
        Guest guest = findGuest(guestId);
        guest.getFavorite().removeIf(accommodation -> Objects.equals(accommodation.getId(), accommodationId));
        guestRepository.save(guest);
        guestRepository.flush();
        return new MessageDto("Favorite removed");
    }


    private Long getGuestId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        if (account.getRole() != AccountRole.GUEST) throw new RuntimeException("User is not a guest");
        return account.getId();
    }
}
