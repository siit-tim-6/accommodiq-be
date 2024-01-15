package com.example.accommodiq.services;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.AccommodationBookingDetailsDto;
import com.example.accommodiq.dtos.AvailabilityDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.enums.AccommodationStatus;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.repositories.ReviewRepository;
import com.example.accommodiq.services.interfaces.accommodations.IAccommodationService;
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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AccommodationServiceTest {
    private final Account validHostAccount = new Account();
    private Accommodation validAccommodation = new Accommodation(1L, "Accommodation 1",
            "Description", null, List.of(), 1, 1,
            null, AccommodationStatus.ACCEPTED, PricingType.PER_GUEST, true, 1, null);
    @MockBean
    private AccommodationRepository accommodationRepository;
    @MockBean
    private IGuestService guestService;
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private INotificationService notificationService;
    @MockBean
    private IAccountService accountService;
    @Autowired
    private IAccommodationService accommodationService;
    @Mock
    private Authentication authentication;
    @Captor
    private ArgumentCaptor<Reservation> reservationArgumentCaptor;
    @Captor
    private ArgumentCaptor<Accommodation> accommodationArgumentCaptor;

    @BeforeEach
    public void setUp() {
        validHostAccount.setRole(AccountRole.HOST);
        validHostAccount.setId(1L);

        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(AccountRole.HOST);
        Authentication authentication = new UsernamePasswordAuthenticationToken(validHostAccount, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        validAccommodation.setAvailable(createAvailabilityList());
    }

    private Set<Availability> createAvailabilityList() {
        // Create a HashSet for availabilities
        Set<Availability> availabilities = new HashSet<>();

        // Create Availability instances
        Availability availability1 = new Availability(1L, 1622559600000L, 1622646000000L, 100.0);
        Availability availability2 = new Availability(2L, 1622732400000L, 1622818800000L, 150.0);
        Availability availability3 = new Availability(3L, 1622905200000L, 1622991600000L, 120.0);

        // Add Availability instances to HashSet
        availabilities.add(availability1);
        availabilities.add(availability2);
        availabilities.add(availability3);

        return availabilities;
    }

    public static Stream<Arguments> provideValidAvailabilityScenarios() {
        return Stream.of(
                // After all existing availabilities
                Arguments.of(new AvailabilityDto(1622991600000L + 100000, 1622991600000L + 100000 + 86400000, 130.0)),
                // Between availability1 and availability2
                Arguments.of(new AvailabilityDto(1622646000000L + 100000, 1622732400000L, 140.0)),
                // Before all existing availabilities
                Arguments.of(new AvailabilityDto(1622559600000L - 86400000, 1622559600000L - 3600000, 110.0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidAvailabilityScenarios")
    @DisplayName("Should Add Accommodation Availability Successfully")
    public void testAddAccommodationAvailabilitySuccess(AvailabilityDto availabilityDto) {
        int defaultAvailabilityCount = validAccommodation.getAvailable().size();

        // Mocking
        when(accommodationRepository.findById(validAccommodation.getId())).thenReturn(Optional.ofNullable(validAccommodation));

        // Act
        ResponseEntity<List<Availability>> response = accommodationService.addAccommodationAvailability(validAccommodation.getId(), availabilityDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(containsAvailabilityWithSameProperties(Objects.requireNonNull(response.getBody()), availabilityDto));
        assertEquals(defaultAvailabilityCount + 1, response.getBody().size());
        verify(accommodationRepository).save(accommodationArgumentCaptor.capture());
        verify(accommodationRepository).flush();
        assertSame(validAccommodation, accommodationArgumentCaptor.getValue());
        verify(accommodationRepository,times(2)).findById(validAccommodation.getId());
        verifyNoInteractions(guestService);
        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(accountService);
    }

    public static Stream<Arguments> provideOverlappingAvailabilityScenarios() {
        return Stream.of(
                // Overlaps with availability1
                Arguments.of(new AvailabilityDto(1622559600000L, 1622646000000L + 100000, 130.0)),
                // Overlaps with availability2
                Arguments.of(new AvailabilityDto(1622732400000L - 100000, 1622818800000L + 100000, 140.0)),
                // Overlaps with availability3
                Arguments.of(new AvailabilityDto(1622905200000L - 100000, 1622991600000L + 100000, 150.0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOverlappingAvailabilityScenarios")
    @DisplayName("Should Not Add Accommodation Availability Due to Overlapping")
    public void testAddAccommodationAvailabilityUnsuccessful(AvailabilityDto availabilityDto) {
        int defaultAvailabilityCount = validAccommodation.getAvailable().size();

        // Mocking
        when(accommodationRepository.findById(validAccommodation.getId())).thenReturn(Optional.ofNullable(validAccommodation));

        // Act
        ResponseEntity<List<Availability>> response = accommodationService.addAccommodationAvailability(validAccommodation.getId(), availabilityDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(defaultAvailabilityCount, validAccommodation.getAvailable().size());
        verify(accommodationRepository).findById(validAccommodation.getId());
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(guestService);
        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(accountService);
    }

    @Test
    @DisplayName("Should Not Add Accommodation Availability Due to Invalid Accommodation ID")
    public void testRemoveAccommodationAvailabilityNotFound() {
        Long availabilityId = 99L; // Non-existing ID

        // Mocking
        when(accommodationRepository.findById(validAccommodation.getId())).thenReturn(Optional.ofNullable(validAccommodation));

        // Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accommodationService.removeAccommodationAvailability(validAccommodation.getId(), availabilityId);
        });

        // Assert
        assertEquals("Availability not found", exception.getReason());
        verify(accommodationRepository).findById(validAccommodation.getId());
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(guestService);
        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(accountService);
    }

    @Test
    @DisplayName("Should Not Add Accommodation Availability Due to Active Reservations")
    public void testRemoveAccommodationAvailabilityWithActiveReservations() {
        Availability availability = new Availability(4L,1622991600000L + 100000, 1622991600000L + 100000 + 86400000, 130.0);
        validAccommodation.getAvailable().add(availability);

        // Mocking
        when(accommodationRepository.findById(validAccommodation.getId())).thenReturn(Optional.ofNullable(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, validAccommodation.getId(), availability.getFromDate(), availability.getToDate(), List.of(ReservationStatus.ACCEPTED))).thenReturn(1L);

        // Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accommodationService.removeAccommodationAvailability(validAccommodation.getId(), availability.getId());
        });

        // Assert
        assertEquals("Cannot remove availability as there are active reservations in this period.", exception.getReason());
        verify(accommodationRepository).findById(validAccommodation.getId());
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(null, validAccommodation.getId(), availability.getFromDate(), availability.getToDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(guestService);
        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(accountService);
    }

    @Test
    @DisplayName("Should Not Add Accommodation Availability Due to Invalid Accommodation ID")
    public void testRemoveAccommodationAvailabilityWithNullIds() {
        assertThrows(ResponseStatusException.class, () -> {
            accommodationService.removeAccommodationAvailability(null, null);
        });
    }


    public static Stream<Arguments> provideValidAvailabilityRemoveScenarios() {
        return Stream.of(
                // After all existing availabilities
                Arguments.of(new Availability(4L,1622991600000L + 100000, 1622991600000L + 100000 + 86400000, 130.0)),
                // Between availability1 and availability2
                Arguments.of(new Availability(4L,1622646000000L + 100000, 1622732400000L, 140.0)),
                // Before all existing availabilities
                Arguments.of(new Availability(4L,1622559600000L - 86400000, 1622559600000L - 3600000, 110.0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidAvailabilityRemoveScenarios")
    @DisplayName("Should Remove Accommodation Availability Successfully")
    public void testRemoveAccommodationAvailabilitySuccess(Availability availability) {
        int defaultAvailabilityCount = validAccommodation.getAvailable().size();
        validAccommodation.getAvailable().add(availability);

        // Mocking
        when(accommodationRepository.findById(validAccommodation.getId())).thenReturn(Optional.ofNullable(validAccommodation));
        when(reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, validAccommodation.getId(), availability.getFromDate(), availability.getToDate(), List.of(ReservationStatus.ACCEPTED))).thenReturn(0L);

        // Act
        MessageDto response = accommodationService.removeAccommodationAvailability(validAccommodation.getId(), availability.getId());

        // Assert
        assertEquals("Availability removed", response.getMessage());
        assertEquals(validAccommodation.getAvailable().size(), defaultAvailabilityCount);
        assertTrue(validAccommodation.getAvailable().stream().noneMatch(a -> a.getId().equals(availability.getId())));
        verify(accommodationRepository,times(2)).findById(validAccommodation.getId());
        verify(accommodationRepository).save(accommodationArgumentCaptor.capture());
        verify(accommodationRepository).flush();
        assertSame(validAccommodation, accommodationArgumentCaptor.getValue());
        verify(reservationRepository).countOverlappingReservationsOrGuestOverlappingReservations(null, validAccommodation.getId(), availability.getFromDate(), availability.getToDate(), List.of(ReservationStatus.ACCEPTED));
        verifyNoInteractions(guestService);
        verifyNoInteractions(reviewRepository);
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(accountService);
    }

    @Test
    @DisplayName("Should Update Accommodation Successfully")
    public void testUpdateAccommodationBookingDetailsSuccess() {
        // Arrange
        AccommodationBookingDetailsDto bookingDetailsDto = new AccommodationBookingDetailsDto();
        bookingDetailsDto.setCancellationDeadline(48);
        bookingDetailsDto.setPricingType(PricingType.PER_GUEST);

        // Mocking
        when(accommodationRepository.findById(validAccommodation.getId())).thenReturn(Optional.ofNullable(validAccommodation));

        // Act
        ResponseEntity<AccommodationBookingDetailsDto> response = accommodationService.updateAccommodationBookingDetails(validAccommodation.getId(), bookingDetailsDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bookingDetailsDto.getCancellationDeadline(), response.getBody().getCancellationDeadline());
        assertEquals(bookingDetailsDto.getPricingType(), response.getBody().getPricingType());

        verify(accommodationRepository).save(accommodationArgumentCaptor.capture());
        verify(accommodationRepository).flush();
        assertSame(validAccommodation, accommodationArgumentCaptor.getValue());
        verify(accommodationRepository, times(2)).findById(validAccommodation.getId());
        verifyNoInteractions(guestService);
        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(accountService);
        verifyNoMoreInteractions(accommodationRepository);
    }

    @Test
    @DisplayName("Should Not Update Accommodation Due to Invalid Accommodation ID")
    public void testUpdateAccommodationBookingDetailsNotFound() {
        // Arrange
        Long accommodationId = 99L; // Non-existing ID
        AccommodationBookingDetailsDto bookingDetailsDto = new AccommodationBookingDetailsDto();
        bookingDetailsDto.setCancellationDeadline(48);
        bookingDetailsDto.setPricingType(PricingType.PER_GUEST);

        // Mocking
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.ofNullable(null));

        // Act
        assertThrows(ResponseStatusException.class, () -> {
            accommodationService.updateAccommodationBookingDetails(accommodationId, bookingDetailsDto);
        });

        // Assert
        verify(accommodationRepository).findById(accommodationId);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(guestService);
        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(accountService);
    }
    
    private boolean containsAvailabilityWithSameProperties(List<Availability> availabilities, AvailabilityDto dto) {
        return availabilities.stream().anyMatch(a ->
                Objects.equals(a.getFromDate(), dto.getFromDate()) &&
                        Objects.equals(a.getToDate(), dto.getToDate()) &&
                        a.getPrice() == dto.getPrice());
    }


}
