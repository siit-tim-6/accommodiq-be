package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.NotificationDto;
import com.example.accommodiq.dtos.ReservationCardDto;
import com.example.accommodiq.dtos.ReservationStatusDto;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.utilities.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HostControllerTest {
    public static final long activeReservationId = 1L;
    public static final String hostEmail = "john.doe@example.com";
    public static final String password = "123";

    public static final String blockedHostEmail = "jane.smith@example.com";
    private final String guestEmail = "guest.bj@example.com";
    public static final String changeReservationStatusUrl = "/reservations/{reservationId}/status";
    public static final String fetchHostReservationsUrl = "/hosts/reservations";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should return Forbidden status when user is not host")
    public void test(){
        ReservationStatusDto reservationStatusDto = new ReservationStatusDto(ReservationStatus.ACCEPTED);
        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), guestEmail, password);
        HttpEntity<ReservationStatusDto> requestEntity = new HttpEntity<>(reservationStatusDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                changeReservationStatusUrl.replace("{reservationId}", "1"), HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Guest can only cancel reservation", getResponseMessage(response.getBody()));
    }

    private static Stream<Arguments> provideReservationStatuses() {
        return Stream.of(
                Arguments.of(ReservationStatus.CANCELLED),
                Arguments.of(ReservationStatus.PENDING)
        );
    }

    private static Stream<Arguments> provideReservationStatuses2() {
        return Stream.of(
                Arguments.of(ReservationStatus.ACCEPTED),
                Arguments.of(ReservationStatus.DECLINED)
        );
    }

    @ParameterizedTest
    @MethodSource("provideReservationStatuses")
    @DisplayName("Should return Forbidden status when host tries to Cancel or Pend reservation")
    public void test2(ReservationStatus reservationStatus) {
        ReservationStatusDto reservationStatusDto = new ReservationStatusDto(reservationStatus);
        HttpEntity<ReservationStatusDto> requestEntity = createStandardRequestEntity(reservationStatusDto);
        ResponseEntity<String> response = restTemplate.exchange(
                putIdInUrl(activeReservationId), HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Host can only accept or decline reservation", getResponseMessage(response.getBody()));
        assertFalse(isReservationStatusChanged(activeReservationId, reservationStatus));
    }

    @ParameterizedTest
    @MethodSource("provideReservationStatuses2")
    @DisplayName("Should change reservation status")
    public void test3(ReservationStatus reservationStatus) {
        long reservationId = 1L;
        ReservationStatusDto reservationStatusDto = new ReservationStatusDto(reservationStatus);
        HttpEntity<ReservationStatusDto> requestEntity = createStandardRequestEntity(reservationStatusDto);
        ResponseEntity<ReservationCardDto> response = restTemplate.exchange(
                putIdInUrl(reservationId), HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reservationId, response.getBody().getId());
        assertSame(reservationStatus, response.getBody().getStatus());
        assertTrue(isReservationStatusChanged(reservationId, reservationStatus));
    }



    @Test
    @DisplayName("Should return NOT_FOUND when reservation not found")
    public void testAccommodationNotFound() {
        long reservationId = 100L;
        ReservationStatusDto reservationStatusDto = new ReservationStatusDto(ReservationStatus.ACCEPTED);
        HttpEntity<ReservationStatusDto> requestEntity = createStandardRequestEntity(reservationStatusDto);

        ResponseEntity<String> response = restTemplate.exchange(
                putIdInUrl(reservationId), HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Reservation not found", getResponseMessage(response.getBody()));
    }


    @Test
    @DisplayName("Should accept reservation and decline overlapping reservation")
    public void test4() {
        long reservationId = 9L;
        ReservationStatusDto reservationStatusDto = new ReservationStatusDto(ReservationStatus.ACCEPTED);
        HttpEntity<ReservationStatusDto> requestEntity = createStandardRequestEntity(reservationStatusDto);
        ResponseEntity<ReservationCardDto> response = restTemplate.exchange(
                putIdInUrl(reservationId), HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reservationId, response.getBody().getId());
        assertSame(ReservationStatus.ACCEPTED, response.getBody().getStatus());
        assertTrue(isReservationStatusChanged(reservationId, ReservationStatus.ACCEPTED));
        assertTrue(isReservationStatusChanged(10L, ReservationStatus.DECLINED));
        assertTrue(isReservationStatusChanged(8L, ReservationStatus.PENDING));
    }

    private String putIdInUrl(long reservationId) {
        return changeReservationStatusUrl.replace("{reservationId}", String.valueOf(reservationId));
    }

    private HttpEntity<ReservationStatusDto> createStandardRequestEntity(ReservationStatusDto reservationStatusDto) {
        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), hostEmail, password);
        return (new HttpEntity<>(reservationStatusDto, headers));
    }

    private String getResponseMessage(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonResponse = objectMapper.readTree(body);
            return jsonResponse.get("message").asText();
        } catch (JsonProcessingException ex) {
            return "";
        }
    }

    private boolean isReservationStatusChanged(long reservationId, ReservationStatus reservationStatus) {
        return fetchHostReservations().stream().anyMatch(reservation -> reservation.getId() == reservationId && reservation.getStatus() == reservationStatus);
    }

    private ReservationStatus getReservationStatus(long reservationId) {
        return fetchHostReservations().stream().filter(reservation -> reservation.getId() == reservationId).findFirst().get().getStatus();
    }

    private List<ReservationCardDto> fetchHostReservations() {
        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), hostEmail, password);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<ReservationCardDto>> response = restTemplate.exchange(
                fetchHostReservationsUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

    private List<NotificationDto> fetchGuestNotifications() {
        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), guestEmail, password);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<NotificationDto>> response = restTemplate.exchange(
                "/guests/notifications", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }
}