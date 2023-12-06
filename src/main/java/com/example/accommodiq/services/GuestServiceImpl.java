package com.example.accommodiq.services;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.services.interfaces.IGuestService;
import com.example.accommodiq.utilities.ReportUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
public class GuestServiceImpl implements IGuestService {
    @Override
    public Collection<ReservationListDto> getReservations(Long guestId) {
        if (guestId == 4L) {
            ReportUtils.throwNotFound("guestNotFound");
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

    @Override
    public Reservation addReservation(Long guestId, ReservationRequestDto reservationDto) {
        if (guestId == 4L) {
            ReportUtils.throwNotFound("guestNotFound");
        }

        return new Reservation(
                1L,
                new Date().getTime(),
                new Date().getTime(),
                2,
                ReservationStatus.CREATED,
                null,
                null
        );
    }

    @Override
    public Collection<AccommodationListDto> getFavorites(Long guestId) {
        if (guestId == 4L) {
            ReportUtils.throwNotFound("guestNotFound");
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
            ReportUtils.throwNotFound("guestNotFound");
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
            ReportUtils.throwNotFound("guestNotFound");
        }

        if (accommodationId == 4L) {
            ReportUtils.throwNotFound("favoriteNotFound");
        }

        return new MessageDto("Favorite removed successfully");
    }
}
