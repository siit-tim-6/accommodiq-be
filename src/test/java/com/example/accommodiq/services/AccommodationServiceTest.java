package com.example.accommodiq.services;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.AvailabilityDto;
import com.example.accommodiq.enums.AccommodationStatus;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.enums.PricingType;
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
        Long accommodationId = 1L;

        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.ofNullable(validAccommodation));

        ResponseEntity<List<Availability>> response = accommodationService.addAccommodationAvailability(accommodationId, availabilityDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(containsAvailabilityWithSameProperties(Objects.requireNonNull(response.getBody()), availabilityDto));
        verify(accommodationRepository).save(accommodationArgumentCaptor.capture());
        verify(accommodationRepository).flush();
        assertSame(validAccommodation, accommodationArgumentCaptor.getValue());
        verify(accommodationRepository,times(2)).findById(accommodationId);
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
        Long accommodationId = 1L;

        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.ofNullable(validAccommodation));

        ResponseEntity<List<Availability>> response = accommodationService.addAccommodationAvailability(accommodationId, availabilityDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
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
