package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.enums.AccommodationStatus;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.enums.ReservationStatus;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BGuestServiceTest {
    private final Guest validGuest = new Guest(1L, "John", "Doe", "address", "1234");
    private final Long validGuestId = 1L;
    private final Long invalidGuestId = 2L;
    private final Accommodation validAccommodationWithAutomaticAcceptance = new Accommodation(1L, "Accommodation 1",
            "Description", null, null, 1, 1,
            null, AccommodationStatus.ACCEPTED, PricingType.PER_GUEST, true, 1, null);
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

    @MockBean
    private INotificationService notificationService;

    @Autowired
    private IGuestService guestService;

    @Captor
    private ArgumentCaptor<Guest> guestArgumentCaptor;

    private final Account validGuestAccount = new Account(new RegisterDto(AccountRole.GUEST, "bla.bla@hotmail.com", "$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC", validGuest));


    @BeforeEach
    public void setUp() {
        validGuest.setReservations(new HashSet<>());

        validGuestAccount.setId(1L);
        validGuestAccount.setRole(AccountRole.GUEST);
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(AccountRole.GUEST);
        Authentication authentication = new UsernamePasswordAuthenticationToken(validGuestAccount, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        validAccommodationWithAutomaticAcceptance.setAvailable(new HashSet<>(List.of(
                new Availability(1L, 0L, 10L, 200.0)
        )));
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Test should throw Not found exception when accommodation not found")
    public void testShouldThrowResponseStatusExceptionWhenAccommodationNotFound() {
        // Arrange
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);

        when(guestRepository.findById(validGuestId)).thenReturn(Optional.of(validGuest));
        when(accommodationRepository.findById(invalidAccommodationId)).thenReturn(Optional.empty());
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(0, 1, 1, invalidAccommodationId);
        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> guestService.addReservation(reservationRequestDto)
        );
        // Assert
        assertEquals("404 NOT_FOUND \"Accommodation not found\"", exception.getMessage());
        verify(guestRepository).findById(validGuestId);
        verify(accommodationRepository).findById(invalidAccommodationId);
        verifyNoMoreInteractions(guestRepository);
        verifyNoMoreInteractions(accommodationRepository);

        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(notificationService);

    }

    @Test
    @DisplayName("Test should throw Not found exception when guest not found")
    public void testShouldThrowResponseStatusExceptionWhenGuestNotFound() {
        // Arrange
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.empty());
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodationWithAutomaticAcceptance.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("404 NOT_FOUND \"Guest not found\"", exception.getMessage());

        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Test should accept reservation")
    public void testShouldAutomaticAcceptReservation() {
        //Arrange
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        when(accommodationRepository.findById(accommodationWithAutomaticAcceptanceId)).thenReturn(Optional.of(validAccommodationWithAutomaticAcceptance));
        when(guestRepository.findById(validGuestId)).thenReturn(Optional.of(validGuest));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(any(), any(), any(), any(), any())).thenReturn(0L);
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(0, 1, 1, accommodationWithAutomaticAcceptanceId);

        //Act
        ReservationRequestDto res = guestService.addReservation(reservationRequestDto);

        //Assert
        verify(accommodationRepository, times(2)).findById(accommodationWithAutomaticAcceptanceId);
        verify(guestRepository).findById(validGuestId);
        verify(reservationRepository, times(2)).countOverlappingReservationsOrGuestOverlappingReservations(any(), any(), any(), any(), any());
        verify(guestRepository).save(guestArgumentCaptor.capture());
        verify(guestRepository).flush();

        assertEquals(1, guestArgumentCaptor.getValue().getReservations().size());
        assertSame(guestArgumentCaptor.getValue().getReservations().stream().findFirst().get().getStatus(), ReservationStatus.ACCEPTED);
        assertEquals(res, reservationRequestDto);

        verifyNoMoreInteractions(accommodationRepository);
        verifyNoMoreInteractions(guestRepository);
        verifyNoMoreInteractions(reservationRepository);

        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
    }

    @Test
    @DisplayName("Test should not accept reservation")
    public void testShouldNotAutomaticAcceptReservation() {
        //Arrange
        when(accommodationRepository.findById(accommodationWithManualAcceptanceId)).thenReturn(Optional.of(validAccommodationWithManualAcceptance));
        when(guestRepository.findById(validGuestId)).thenReturn(Optional.of(validGuest));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(any(), any(), any(), any(), any())).thenReturn(0L);
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        validAccommodationWithManualAcceptance.setAvailable(new HashSet<>(List.of(
                new Availability(1L, 0L, 10L, 200.0)
        )));
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(0, 1, 1, accommodationWithManualAcceptanceId);

        //Act
        ReservationRequestDto res = guestService.addReservation(reservationRequestDto);

        //Assert
        verify(accommodationRepository, times(2)).findById(accommodationWithManualAcceptanceId);
        verify(guestRepository).findById(validGuestId);
        verify(reservationRepository, times(2)).countOverlappingReservationsOrGuestOverlappingReservations(any(), any(), any(), any(), any());
        verify(guestRepository).save(guestArgumentCaptor.capture());
        verify(guestRepository).flush();

        assertEquals(1L, guestArgumentCaptor.getValue().getReservations().size());
        assertSame(guestArgumentCaptor.getValue().getReservations().stream().findFirst().get().getStatus(), ReservationStatus.PENDING);
        assertEquals(res, reservationRequestDto);

        verifyNoMoreInteractions(accommodationRepository);
        verifyNoMoreInteractions(guestRepository);
        verifyNoMoreInteractions(reservationRepository);

        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);

    }

    @Test
    @DisplayName("Test should throw not available reservation")
    public void testShouldThrowNotAvailableReservation() {
        //Arrange
        when(accommodationRepository.findById(accommodationWithManualAcceptanceId)).thenReturn(Optional.of(validAccommodationWithManualAcceptance));
        when(guestRepository.findById(validGuestId)).thenReturn(Optional.of(validGuest));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(any(), any(), any(), any(), any())).thenReturn(0L);
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(0, 1, 1, accommodationWithManualAcceptanceId);

        //Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(reservationRequestDto));

        //Assert
        assertEquals("400 BAD_REQUEST \"Accommodation is not available within provided date range\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository, times(2)).findById(reservationRequestDto.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), reservationRequestDto.getAccommodationId(), reservationRequestDto.getStartDate(), reservationRequestDto.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                null, reservationRequestDto.getAccommodationId(), reservationRequestDto.getStartDate(), reservationRequestDto.getEndDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception when start date is after end date")
    public void testInvalidDateRange() {
        ReservationRequestDto newReservation = new ReservationRequestDto(1, 0, 1, validAccommodationWithAutomaticAcceptance.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("400 BAD_REQUEST \"Invalid date range!\"", exception.getMessage());
        verifyNoInteractions(guestRepository);
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(accountService);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(accountService);
    }

    @Test
    @DisplayName("Should throw exception when user not guest")
    public void testWhenUserNotGuest() {
        validGuestAccount.setRole(AccountRole.HOST);
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodationWithAutomaticAcceptance.getId());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> guestService.addReservation(newReservation));

        assertEquals("User is not a guest", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(guestRepository);
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }
}
