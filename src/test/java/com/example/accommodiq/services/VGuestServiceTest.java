package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Location;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.enums.AccommodationStatus;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.GuestRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.notifications.INotificationService;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.users.IGuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class VGuestServiceTest {
    @MockBean
    private GuestRepository guestRepository;
    @MockBean
    private AccommodationRepository accommodationRepository;
    @MockBean
    private IAccountService accountService;
    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private INotificationService notificationService;
    @Autowired
    private IGuestService guestService;
    private final Guest validGuest = new Guest(1L, "Srbin", "Laden", "address", "247365021");
    private final Host validHost = new Host(2L, "Srbislav", "Ladenovic", "address", "247365021");
    private final Accommodation validAccommodation = new Accommodation(1L, "Test", "Test", new Location("location", 21.0, 21.0), new ArrayList<>(), 1, 3, "Apartment", AccommodationStatus.ACCEPTED, PricingType.PER_GUEST, true, 3, validHost);

    @BeforeEach
    public void setUp() {
        validGuest.setReservations(new HashSet<>());
    }

    @Test
    @DisplayName("Should throw an exception for invalid date range")
    public void testInvalidDateRange() {
        ReservationRequestDto newReservation = new ReservationRequestDto(1, 0, 1, 1L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        System.out.println(exception.getMessage());
        verifyNoInteractions();
    }
}
