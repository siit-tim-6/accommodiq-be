package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.ReservationCardDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
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
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(value = "classpath:data/reservations/reset-reservations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GuestControllerTest {
    private final String guestEmail = "guest.bj@example.com";
    private final String blockedGuestEmail = "guest.blocked@example.com";
    private final String guestPassword = "123";
    private final String reservationsUrl = "/guests/reservations";

    @Autowired
    private TestRestTemplate restTemplate;

    private static Stream<Arguments> getNewReservations() {
        return Stream.of(
                Arguments.of(new ReservationRequestDto(1709251200000L, 1709337600000L, 1, 3L)),
                Arguments.of(new ReservationRequestDto(1709596800000L, 1709769600000L, 1, 3L)),
                Arguments.of(new ReservationRequestDto(1710028800000L, 1710201600000L, 1, 4L))
        );
    }

    @ParameterizedTest
    @MethodSource("getNewReservations")
    @DisplayName("Should successfully make a new reservation")
    public void testMakeReservationSuccess(ReservationRequestDto newReservation) {
        HttpEntity<ReservationRequestDto> requestEntity = createStandardRequestEntity(newReservation);

        ResponseEntity<ReservationRequestDto> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newReservation, response.getBody());
        assertTrue(fetchUserReservations().stream().anyMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
    }

    @Test
    @DisplayName("Should return BAD_REQUEST with invalid date range message")
    public void testInvalidDateRange() {
        ReservationRequestDto newReservation = new ReservationRequestDto(1709337600000L, 1709251200000L, 1, 3L);
        HttpEntity<ReservationRequestDto> requestEntity = createStandardRequestEntity(newReservation);

        ResponseEntity<String> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid date range!", getResponseMessage(response.getBody()));
        assertTrue(fetchUserReservations().stream().noneMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
    }

    @Test
    @DisplayName("Should return FORBIDDEN with access denied message")
    public void testUnauthorizedAccessDenied() {
        ReservationRequestDto newReservation = new ReservationRequestDto(1709251200000L, 1709337600000L, 1, 3L);
        HttpEntity<ReservationRequestDto> requestEntity = new HttpEntity<>(newReservation);

        ResponseEntity<String> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Access Denied", getResponseMessage(response.getBody()));
        assertTrue(fetchUserReservations().stream().noneMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
    }

//    @Test
//    @DisplayName("Should return FORBIDDEN with account blocked message")
//    public void testAccountBlocked() {
//        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), blockedGuestEmail, guestPassword);
//        ReservationRequestDto newReservation = new ReservationRequestDto(1709251200000L, 1709337600000L, 1, 3L);
//        HttpEntity<ReservationRequestDto> requestEntity = new HttpEntity<>(newReservation, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
//                }
//        );
//
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("Your account is blocked!", getResponseMessage(response.getBody()));
//        assertTrue(fetchUserReservations().stream().noneMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
//    }

    @Test
    @DisplayName("Should return FORBIDDEN with forbidden message")
    public void testWrongRoleForbidden() {
        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), "admin", "123");
        ReservationRequestDto newReservation = new ReservationRequestDto(1709251200000L, 1709337600000L, 1, 3L);
        HttpEntity<ReservationRequestDto> requestEntity = new HttpEntity<>(newReservation, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Forbidden", getResponseMessage(response.getBody()));
        assertTrue(fetchUserReservations().stream().noneMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
    }

    @Test
    @DisplayName("Should return NOT_FOUND with accommodation not found message")
    public void testAccommodationNotFound() {
        ReservationRequestDto newReservation = new ReservationRequestDto(1709251200000L, 1709337600000L, 1, 100L);
        HttpEntity<ReservationRequestDto> requestEntity = createStandardRequestEntity(newReservation);

        ResponseEntity<String> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Accommodation not found", getResponseMessage(response.getBody()));
        assertTrue(fetchUserReservations().stream().noneMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
    }

    private static Stream<Arguments> getOverlappingReservations() {
        return Stream.of(
                Arguments.of(new ReservationRequestDto(1709683200000L, 1709856000000L, 1, 4L)),
                Arguments.of(new ReservationRequestDto(1709510400000L, 1709683200000L, 1, 3L))
        );
    }

    @ParameterizedTest
    @MethodSource("getOverlappingReservations")
    @DisplayName("Should return BAD_REQUEST with guest overlapping message")
    public void testGuestSelfOverlapping(ReservationRequestDto newReservation) {
        HttpEntity<ReservationRequestDto> requestEntity = createStandardRequestEntity(newReservation);

        ResponseEntity<String> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User has an overlapping reservation", getResponseMessage(response.getBody()));
        assertTrue(fetchUserReservations().stream().noneMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
    }

    @Test
    @DisplayName("Should return BAD_REQUEST with accommodation unavailable message")
    public void testOtherOverlapping() {
        ReservationRequestDto newReservation = new ReservationRequestDto(1709337600000L, 1709596800000L, 1, 4L);
        HttpEntity<ReservationRequestDto> requestEntity = createStandardRequestEntity(newReservation);

        ResponseEntity<String> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Accommodation is not available within provided date range", getResponseMessage(response.getBody()));
        assertTrue(fetchUserReservations().stream().noneMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
    }

    private static Stream<Arguments> getReservationsWithoutAvailabilities() {
        return Stream.of(
                Arguments.of(new ReservationRequestDto(1709683200000L, 1709856000000L, 1, 5L)),
                Arguments.of(new ReservationRequestDto(1709856000000L, 1710028800000L, 1, 6L)),
                Arguments.of(new ReservationRequestDto(1709251200000L, 1709769600000L, 1, 6L)),
                Arguments.of(new ReservationRequestDto(1709596800000L, 1709856000000L, 1, 6L))
        );
    }

    @ParameterizedTest
    @MethodSource("getReservationsWithoutAvailabilities")
    @DisplayName("Should return BAD_REQUEST with accommodation unavailable message")
    public void testAccommodationNoRightAvailabilities(ReservationRequestDto newReservation) {
        HttpEntity<ReservationRequestDto> requestEntity = createStandardRequestEntity(newReservation);

        ResponseEntity<String> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Accommodation is not available within provided date range", getResponseMessage(response.getBody()));
        assertTrue(fetchUserReservations().stream().noneMatch(reservationCardDto -> compareReservation(newReservation, reservationCardDto)));
    }

    @Test
    @DisplayName("Should return OK with reservation being accepted")
    public void testAutomaticAcceptance() {
        ReservationRequestDto newReservation = new ReservationRequestDto(1710201600000L, 1710374400000L, 1, 4L);
        HttpEntity<ReservationRequestDto> requestEntity = createStandardRequestEntity(newReservation);

        ResponseEntity<ReservationRequestDto> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newReservation, response.getBody());
        List<ReservationCardDto> filteredReservations = fetchUserReservations().stream().filter(reservationCardDto -> compareReservation(newReservation, reservationCardDto)).toList();
        assertFalse(filteredReservations.isEmpty());
        assertNotNull(filteredReservations.get(0));
        assertEquals(filteredReservations.get(0).getStatus(), ReservationStatus.ACCEPTED);
    }


    // TODO: invalid guest number i null za guests kada je PER_GUEST

    // --------------------- PRIVATE METHODS -----------------------------------

    private HttpEntity<ReservationRequestDto> createStandardRequestEntity(ReservationRequestDto reservationRequestDto) {
        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), guestEmail, guestPassword);
        return (new HttpEntity<>(reservationRequestDto, headers));
    }

    private List<ReservationCardDto> fetchUserReservations() {
        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), guestEmail, guestPassword);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<ReservationCardDto>> response = restTemplate.exchange(
                reservationsUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

    private boolean compareReservation(ReservationRequestDto reservationRequestDto, ReservationCardDto reservationCardDto) {
        return reservationRequestDto.getStartDate() == reservationCardDto.getStartDate()
                && reservationRequestDto.getEndDate() == reservationCardDto.getEndDate()
                && reservationRequestDto.getAccommodationId() == reservationCardDto.getAccommodationId()
                && reservationRequestDto.getNumberOfGuests() == reservationCardDto.getGuests();
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
}
