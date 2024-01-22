package com.example.accommodiq.services;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.ReservationCardDto;
import com.example.accommodiq.enums.*;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.repositories.ReviewRepository;
import com.example.accommodiq.services.interfaces.accommodations.IReservationService;
import com.example.accommodiq.services.interfaces.notifications.INotificationService;
import com.example.accommodiq.services.interfaces.users.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationServiceTest {
    private static final Long INVALID_RESERVATION_ID = 2L;
    private final Account validHostAccount = new Account();
    private final Guest validGuest = new Guest();
    private Accommodation validAccommodation = new Accommodation(1L, "Accommodation 1",
            "Description", null, List.of(), 1, 1,
            null, AccommodationStatus.ACCEPTED, PricingType.PER_GUEST, true, 1, null);
    private final Reservation validReservation = new Reservation(1L, 100L, 200L, 1, ReservationStatus.PENDING, validGuest, validAccommodation, 0);
    private final Long VALID_RESERVATION_ID = 1L;
    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private AccommodationRepository accommodationRepository;
    @MockBean
    private IUserService userService;
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private INotificationService notificationService;
    @Autowired
    private IReservationService reservationService;
    @Mock
    private Authentication authentication;
    @Captor
    private ArgumentCaptor<Reservation> reservationArgumentCaptor;
    @Captor
    private ArgumentCaptor<Notification> notificationArgumentCaptor;

    @BeforeEach
    public void setup() {
        validHostAccount.setRole(AccountRole.HOST);
        validHostAccount.setId(1L);

        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(AccountRole.HOST);
        Authentication authentication = new UsernamePasswordAuthenticationToken(validHostAccount, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        validAccommodation.setAvailable(new HashSet<>());
    }

    @ParameterizedTest
    @DisplayName("Test should Throw Forbidden Exception when Host tries to set wrong status")
    @EnumSource(value = ReservationStatus.class, names = {"CANCELLED", "PENDING"})
    public void testShouldThrowForbiddenExceptionWhenHostTriesToCancelReservation(ReservationStatus status) {
        // Act
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            reservationService.changeReservationStatus(1L, status);
        });

        // Assert
        assertEquals("403 FORBIDDEN \"Host can only accept or decline reservation\"", ex.getMessage());
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(userService);
        verifyNoInteractions(accommodationRepository);
    }

    @Test
    @DisplayName("Test should Throw Not Found Exception when Reservation does not exist")
    public void testShouldThrowNotFoundExceptionWhenReservationDoesNotExist() {
        when(reservationRepository.findById(INVALID_RESERVATION_ID)).thenReturn(Optional.empty());
        // Act
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            reservationService.changeReservationStatus(INVALID_RESERVATION_ID, ReservationStatus.ACCEPTED);
        });

        // Assert
        assertEquals("404 NOT_FOUND \"Reservation not found\"", ex.getMessage());
        verify(reservationRepository).findById(INVALID_RESERVATION_ID);
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(notificationService);
        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(userService);
        verifyNoInteractions(accommodationRepository);
    }

    @Test
    @DisplayName("Test should create Reservation card Dto when Reservation is Declined")
    public void testShouldCreateReservationCardDtoWhenReservationStatusIsNotAccepted() {
        // Arrange
        when(reservationRepository.findById(VALID_RESERVATION_ID)).thenReturn(Optional.of(validReservation));
        validAccommodation.getAvailable().add(new Availability(null, 0L, 2000000000L, 1000));
        // Act
        ReservationCardDto card = reservationService.changeReservationStatus(VALID_RESERVATION_ID, ReservationStatus.DECLINED);
        // Assert
        verify(reservationRepository).findById(VALID_RESERVATION_ID);
        verify(reservationRepository).save(reservationArgumentCaptor.capture());
        verify(reservationRepository).flush();
        assertSame(ReservationStatus.DECLINED, reservationArgumentCaptor.getValue().getStatus());
        verifyNoMoreInteractions(reservationRepository);

        verify(notificationService).createAndSendNotification(notificationArgumentCaptor.capture());
        notificationAssertions(notificationArgumentCaptor.getValue(), " has been declined");
        verifyNoMoreInteractions(notificationService);

        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(userService);
        verifyNoInteractions(accommodationRepository);

        reservationCardAssertions(card);

    }

    @Test
    @DisplayName("Test should throw Bad Request when accommodation is not available")
    public void testShouldThrowBadRequestWhenAccommodationIsNotAvailable() {
        // Arrange
        when(reservationRepository.findById(VALID_RESERVATION_ID)).thenReturn(Optional.of(validReservation));
        // Act
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            reservationService.changeReservationStatus(VALID_RESERVATION_ID, ReservationStatus.DECLINED);
        });
        // Assert
        assertEquals("400 BAD_REQUEST \"Accommodation is not available within provided date range\"", ex.getMessage());
        verify(reservationRepository).findById(VALID_RESERVATION_ID);
        verify(reservationRepository).save(reservationArgumentCaptor.capture());
        verify(reservationRepository).flush();
        assertSame(ReservationStatus.DECLINED, reservationArgumentCaptor.getValue().getStatus());
        verifyNoMoreInteractions(reservationRepository);

        verify(notificationService).createAndSendNotification(notificationArgumentCaptor.capture());
        notificationAssertions(notificationArgumentCaptor.getValue(), " has been declined");
        verifyNoMoreInteractions(notificationService);

        verifyNoInteractions(reviewRepository);
        verifyNoInteractions(userService);
        verifyNoInteractions(accommodationRepository);
    }

    @Test
    @DisplayName("Test should create Reservation card Dto when Reservation is Accepted")
    public void testShouldCreateReservationCardDtoWhenReservationStatusIsAccepted() {
        // Arrange
        validAccommodation.getAvailable().add(new Availability(null, 0L, 2000000000L, 1000));
        when(reservationRepository.findById(VALID_RESERVATION_ID)).thenReturn(Optional.of(validReservation));
        validAccommodation.getAvailable().add(new Availability(null, 0L, 2000000000L, 1000));
        Collection<Reservation> overlappingReservations = new HashSet<>();
        overlappingReservations.add(new Reservation(2L, 100L, 200L, 1, ReservationStatus.PENDING, validGuest, validAccommodation, 0));
        overlappingReservations.add(new Reservation(2L, 150L, 200L, 1, ReservationStatus.PENDING, validGuest, validAccommodation, 0));
        overlappingReservations.add(new Reservation(2L, 120L, 300L, 1, ReservationStatus.PENDING, validGuest, validAccommodation, 0));
        when(reservationRepository.findByAccommodationIdAndStartDateBetweenOrEndDateBetweenAndStatus(any(), any(), any(), any(), any(), any())).thenReturn(overlappingReservations);

        // Act
        ReservationCardDto card = reservationService.changeReservationStatus(VALID_RESERVATION_ID, ReservationStatus.ACCEPTED);
        // Assert
        {
            verify(reservationRepository).findById(VALID_RESERVATION_ID);
            verify(reservationRepository).findByAccommodationIdAndStartDateBetweenOrEndDateBetweenAndStatus(any(), any(), any(), any(), any(), any());

            verify(reservationRepository, times(4)).save(reservationArgumentCaptor.capture());
            List<Reservation> capturedReservations = reservationArgumentCaptor.getAllValues();
            assertSame(ReservationStatus.ACCEPTED, capturedReservations.get(0).getStatus());
            assertSame(ReservationStatus.DECLINED, capturedReservations.get(1).getStatus());
            assertSame(ReservationStatus.DECLINED, capturedReservations.get(2).getStatus());
            assertSame(ReservationStatus.DECLINED, capturedReservations.get(3).getStatus());


            verify(reservationRepository, times(4)).flush();
            verifyNoMoreInteractions(reservationRepository);

            verify(notificationService, times(4)).createAndSendNotification(notificationArgumentCaptor.capture());
            List<Notification> capturedNotifications = notificationArgumentCaptor.getAllValues();
            notificationAssertions(capturedNotifications.get(3), " has been accepted");

            for (int i = 0; i < 3; i++) {
                notificationAssertions(capturedNotifications.get(i), " has been declined");
            }
            verifyNoMoreInteractions(notificationService);

            verifyNoInteractions(reviewRepository);
            verifyNoInteractions(userService);
            verifyNoInteractions(accommodationRepository);
        }

        reservationCardAssertions(card);
    }

    private void reservationCardAssertions(ReservationCardDto card) {
        assertEquals(validReservation.getId(), card.getId());
        assertEquals(validReservation.getAccommodation().getId(), card.getAccommodationId());
        assertEquals("", card.getAccommodationImage());
        assertEquals(validReservation.getAccommodation().getTitle(), card.getAccommodationTitle());
        assertEquals(validReservation.getAccommodation().getAverageRating(), card.getAccommodationRating());
        assertEquals(validReservation.getAccommodation().getReviews().size(), card.getAccommodationReviewCount());
        assertEquals(validReservation.getAccommodation().getLocation(), card.getAccommodationLocation());
        assertEquals(validReservation.getNumberOfGuests(), card.getGuests());
        assertEquals(validReservation.getStartDate(), card.getStartDate());
        assertEquals(validReservation.getEndDate(), card.getEndDate());
        assertEquals(validReservation.getStatus(), card.getStatus());
        assertEquals(validReservation.getAccommodation().getTotalPrice(validReservation.getStartDate(), validReservation.getEndDate(), validReservation.getNumberOfGuests()), card.getTotalPrice());
    }

    private void notificationAssertions(Notification n, String x) {
        assertSame(n.getType(), NotificationType.HOST_REPLY_TO_REQUEST);
        assertEquals(n.getText(), "Your reservation for accommodation " + validReservation.getAccommodation().getTitle() + x);
        assertEquals(n.getUser().getId(), validReservation.getGuest().getId());
    }
}
