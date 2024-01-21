package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.ReservationCardDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.utilities.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(value = "classpath:data/reservations/reset-reservations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GuestControllerTest {
    private final String guestEmail = "guest.bj@example.com";
    private final String guestPassword = "123";
    private final String reservationsUrl = "/guests/reservations";

    @Autowired
    private TestRestTemplate restTemplate;

    private static Stream<Arguments> getNewReservations() {
        return Stream.of(
                Arguments.of(new ReservationRequestDto(1709251200000L, 1709337600000L, 1, 3L)),
                Arguments.of(new ReservationRequestDto(1709596800000L, 1709769600000L, 1, 3L))
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
