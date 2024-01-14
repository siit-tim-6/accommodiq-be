package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.enums.AccommodationStatus;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.GuestRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.users.IGuestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class GuestServiceTest {
    private final Guest validGuest = new Guest(1L, "John", "Doe", "address", "1234");
    private final Long validGuestId = 1L;
    private final Long invalidGuestId = 2L;
    private final Accommodation validAccommodationWithAutomaticAcceptance = new Accommodation(1L, "Accommodation 1",
            "Description", null, null, 1, 1,
            null, AccommodationStatus.ACCEPTED, PricingType.PER_GUEST, false, 1, null);
    private final Accommodation validAccommodationWithManualAcceptance = new Accommodation(1L, "Accommodation 1",
            "Description", null, null, 1, 1,
            null, AccommodationStatus.ACCEPTED, PricingType.PER_GUEST, false, 1, null);
    private final Long accommodationWithAutomaticAcceptanceId = 1L;
    private final Long accommodationWithManualAcceptanceId = 2L;
    private final Long invalidAccommodationId = 3L;

    @MockBean
    private GuestRepository guestRepository;

    @MockBean
    private AccommodationRepository accommodationRepository;

    @MockBean
    private IAccountService accountService;

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private IGuestService guestService;

    @Test
    @DisplayName("Test should throw Not found exception when accommodation not found")
    public void testShouldThrowResponseStatusExceptionWhenAccommodationNotFound() {
        // Arrange
        when(guestRepository.findById(validGuestId)).thenReturn(Optional.of(validGuest));
        when(accommodationRepository.findById(invalidAccommodationId)).thenReturn(Optional.empty());
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(0, 0, 1, invalidAccommodationId);
        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> guestService.addReservation(validGuestId, reservationRequestDto)
        );
        // Assert
        assertEquals("404 NOT_FOUND \"Accommodation not found\"", exception.getMessage());
        verify(guestRepository).findById(validGuestId);
        verify(accommodationRepository).findById(invalidAccommodationId);
        verifyNoMoreInteractions(guestRepository);
        verifyNoMoreInteractions(accommodationRepository);
    }

    @Test
    @DisplayName("Test should throw Not found exception when guest not found")
    public void testShouldThrowResponseStatusExceptionWhenGuestNotFound() {
        // Arrange
        when(guestRepository.findById(invalidGuestId)).thenReturn(Optional.empty());
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(0, 0, 1, accommodationWithAutomaticAcceptanceId);
        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> guestService.addReservation(invalidGuestId, reservationRequestDto)
        );
        // Assert
        assertEquals("404 NOT_FOUND \"Guest not found\"", exception.getMessage());
        verify(guestRepository).findById(invalidGuestId);
        verifyNoMoreInteractions(guestRepository);
        verifyNoInteractions(accommodationRepository);
    }


}
