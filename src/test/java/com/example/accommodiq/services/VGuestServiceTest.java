package com.example.accommodiq.services;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.enums.*;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

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
    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;
    private final Guest validGuest = new Guest(1L, "Srbin", "Laden", "address", "247365021");
    private final Account validGuestAccount = new Account(new RegisterDto(AccountRole.GUEST, "srbin.laden@hotmail.com", "$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC", validGuest));
    private final Host validHost = new Host(2L, "Srbislav", "Ladenovic", "address", "247365021");
    private final Accommodation validAccommodation = new Accommodation(1L, "Test", "Test", new Location("location", 21.0, 21.0), new ArrayList<>(), 1, 3, "Apartment", AccommodationStatus.ACCEPTED, PricingType.PER_GUEST, true, 3, validHost);

    @BeforeEach
    public void setUp() {
        validGuest.setReservations(new HashSet<>());

        validGuestAccount.setId(1L);
        validGuestAccount.setRole(AccountRole.GUEST);
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(AccountRole.GUEST);
        Authentication authentication = new UsernamePasswordAuthenticationToken(validGuestAccount, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        validAccommodation.setAvailable(new HashSet<>(Arrays.asList(
                new Availability(1L, 86400000L, 259200000L, 200.0),
                new Availability(2L, 518400000L, 691200000L, 200.0),
                new Availability(3L, 777600000L, 950400000L, 200.0),
                new Availability(4L, 1036800000L, 1123200000L, 200.0),
                new Availability(5L, 1296000000L, 1555200000L, 200.0)
        )));
    }

    @Test
    @DisplayName("Should throw an exception for invalid date range")
    public void testInvalidDateRange() {
        ReservationRequestDto newReservation = new ReservationRequestDto(1, 0, 1, validAccommodation.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("400 BAD_REQUEST \"Invalid date range!\"", exception.getMessage());
        verifyNoInteractions(guestRepository);
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(accountService);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception that the user is not a guest")
    public void testUserNotGuest() {
        validGuestAccount.setRole(AccountRole.HOST);
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodation.getId());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> guestService.addReservation(newReservation));

        assertEquals("User is not a guest", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(guestRepository);
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception for user blocked")
    public void testUserBlocked() {
        validGuestAccount.setStatus(AccountStatus.BLOCKED);
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodation.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("403 FORBIDDEN \"Your account is blocked!\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(guestRepository);
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception for guest not found")
    public void testGuestNotFound() {
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.empty());
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodation.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("404 NOT_FOUND \"Guest not found\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception for accommodation not found")
    public void testAccommodationNotFound() {
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("404 NOT_FOUND \"Accommodation not found\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception for overlapping reservations")
    public void testOverlappingReservationsGuestCountGreater() {
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.of(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING)))
                .thenReturn(1L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("400 BAD_REQUEST \"User has an overlapping reservation\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception for accommodation not available (there are other reservations)")
    public void testHasOverlappingReservations() {
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.of(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING)))
                .thenReturn(0L);
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED)))
                .thenReturn(1L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("400 BAD_REQUEST \"Accommodation is not available within provided date range\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception for accommodation not available (availability set is null)")
    public void testAccommodationAvailabilitySetNull() {
        validAccommodation.setAvailable(null);
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(0, 1, 1, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.of(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING)))
                .thenReturn(0L);
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED)))
                .thenReturn(0L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("400 BAD_REQUEST \"Accommodation is not available within provided date range\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository, times(2)).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should throw an exception for accommodation not available (there are no adequate availability candidates)")
    public void testAccommodationNoAvailabilityCandidates() {
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(345600000L, 432000000L, 1, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.of(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING)))
                .thenReturn(0L);
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED)))
                .thenReturn(0L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("400 BAD_REQUEST \"Accommodation is not available within provided date range\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository, times(2)).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    private static Stream<Arguments> getNonMatchableDateRanges() {
        return Stream.of(
                Arguments.of(172800000L, 345600000L),
                Arguments.of(432000000L, 604800000L),
                Arguments.of(172800000L, 604800000L),
                Arguments.of(1209600000L, 1641600000L)
        );
    }

    @ParameterizedTest
    @MethodSource("getNonMatchableDateRanges")
    @DisplayName("Should throw an exception for accommodation not available (there are candidates that don't match)")
    public void testAccommodationNoAvailabilityMatches(Long startDate, Long endDate) {
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(startDate, endDate, 1, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.of(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING)))
                .thenReturn(0L);
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED)))
                .thenReturn(0L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("400 BAD_REQUEST \"Accommodation is not available within provided date range\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository, times(2)).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    private static Stream<Arguments> getMatchableDateRanges() {
        return Stream.of(
                Arguments.of(604800000L, 691200000L),
                Arguments.of(604800000L, 864000000L),
                Arguments.of(777600000L, 950400000L),
                Arguments.of(604800000L, 1123200000L)
        );
    }

    @ParameterizedTest
    @MethodSource("getMatchableDateRanges")
    @DisplayName("Should successfully create a reservation")
    public void testSuccessfulCreation(Long startDate, Long endDate) {
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(startDate, endDate, 1, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.of(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING)))
                .thenReturn(0L);
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED)))
                .thenReturn(0L);
        long oneDay = 24 * 60 * 60 * 1000;

        ReservationRequestDto response = guestService.addReservation(newReservation);

        assertEquals(response, newReservation);
        assertEquals(1, validGuest.getReservations().size());
        Reservation newlyCreated = validGuest.getReservations().stream().toList().get(0);
        assertTrue(checkEquality(newReservation, newlyCreated));
        assertEquals(validAccommodation.isAutomaticAcceptance(), newlyCreated.getStatus() == ReservationStatus.ACCEPTED);
        long days = (endDate - startDate) / oneDay;
        double totalPrice = days * 200.0;
        assertEquals(totalPrice, newlyCreated.getTotalPrice());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verify(guestRepository).save(validGuest);
        verify(guestRepository).flush();
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository, times(2)).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(reservationRepository);
        verify(notificationService).createAndSendNotification(notificationCaptor.capture());
        verifyNoMoreInteractions(notificationService);
        Notification newNotification = notificationCaptor.getValue();
        assertEquals("You have a new reservation for " + validAccommodation.getTitle(), newNotification.getText());
        assertEquals(NotificationType.RESERVATION_REQUEST, newNotification.getType());
        assertEquals(validHost, newNotification.getUser());
    }

    private boolean checkEquality(ReservationRequestDto reservationRequestDto, Reservation reservation) {
        return Objects.equals(reservationRequestDto.getAccommodationId(), reservation.getAccommodation().getId())
                && reservationRequestDto.getNumberOfGuests() == reservation.getNumberOfGuests()
                && reservationRequestDto.getStartDate() == reservation.getStartDate()
                && reservationRequestDto.getEndDate() == reservation.getEndDate();
    }

    @Test
    @DisplayName("Should throw an exception for invalid guest number")
    public void testInvalidGuestNumber() {
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(604800000L, 691200000L, 10, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.of(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING)))
                .thenReturn(0L);
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED)))
                .thenReturn(0L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> guestService.addReservation(newReservation));

        assertEquals("400 BAD_REQUEST \"Invalid guest number\"", exception.getMessage());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository, times(2)).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Should successfully create a reservation that's not accepted")
    public void testNoAutomaticAcceptance() {
        validAccommodation.setAutomaticAcceptance(false);
        when(guestRepository.findById(validGuestAccount.getId())).thenReturn(Optional.of(validGuest));
        when(accountService.loadUserByUsername(validGuestAccount.getUsername())).thenReturn(validGuestAccount);
        when(accountService.findAccountByUserId(validGuest.getId())).thenReturn(validGuestAccount);
        ReservationRequestDto newReservation = new ReservationRequestDto(604800000L, 691200000L, 1, validAccommodation.getId());
        when(accommodationRepository.findById(newReservation.getAccommodationId())).thenReturn(Optional.of(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING)))
                .thenReturn(0L);
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED)))
                .thenReturn(0L);
        long oneDay = 24 * 60 * 60 * 1000;

        ReservationRequestDto response = guestService.addReservation(newReservation);

        assertEquals(response, newReservation);
        assertEquals(1, validGuest.getReservations().size());
        Reservation newlyCreated = validGuest.getReservations().stream().toList().get(0);
        assertTrue(checkEquality(newReservation, newlyCreated));
        assertNotSame(newlyCreated.getStatus(), ReservationStatus.ACCEPTED);
        long days = (newReservation.getEndDate() - newReservation.getStartDate()) / oneDay;
        double totalPrice = days * 200.0;
        assertEquals(totalPrice, newlyCreated.getTotalPrice());
        verify(accountService).loadUserByUsername(validGuestAccount.getEmail());
        verify(accountService).findAccountByUserId(validGuest.getId());
        verifyNoMoreInteractions(accountService);
        verify(guestRepository).findById(validGuestAccount.getId());
        verify(guestRepository).save(validGuest);
        verify(guestRepository).flush();
        verifyNoMoreInteractions(guestRepository);
        verify(accommodationRepository, times(2)).findById(newReservation.getAccommodationId());
        verifyNoMoreInteractions(accommodationRepository);
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                validGuestAccount.getId(), newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING));
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(
                null, newReservation.getAccommodationId(), newReservation.getStartDate(), newReservation.getEndDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(reservationRepository);
        verify(notificationService).createAndSendNotification(notificationCaptor.capture());
        verifyNoMoreInteractions(notificationService);
        Notification newNotification = notificationCaptor.getValue();
        assertEquals("You have a new reservation for " + validAccommodation.getTitle(), newNotification.getText());
        assertEquals(NotificationType.RESERVATION_REQUEST, newNotification.getType());
        assertEquals(validHost, newNotification.getUser());
    }
}
