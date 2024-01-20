package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.utilities.TestUtils;
import org.junit.jupiter.api.Assertions;
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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GuestControllerTest {
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
        String url = "/guests/reservations";
        HttpHeaders headers = TestUtils.createHttpHeaders(restTemplate.getRestTemplate(), "guest.bj@example.com", "123");
        HttpEntity<ReservationRequestDto> requestEntity = new HttpEntity<>(newReservation, headers);

        ResponseEntity<ReservationRequestDto> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newReservation, response.getBody());
    }
}
