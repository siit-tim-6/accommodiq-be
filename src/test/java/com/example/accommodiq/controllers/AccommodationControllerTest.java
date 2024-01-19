package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.PricingType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccommodationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should Add Accommodation Availability Successfully")
    public void testAddAccommodationAvailabilitySuccess() {
        long accommodationId = 1L;
        AvailabilityDto availabilityDto = new AvailabilityDto(1704585600000L+1000L,1704585600000L+90000L,500.0); // Initialize with test data
        String url = "/accommodations/" + accommodationId + "/availabilities";


        HttpHeaders headers = createHttpHeaders();
        HttpEntity<AvailabilityDto> request = new HttpEntity<>(availabilityDto, headers);

        ResponseEntity<List<Availability>> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, request, new ParameterizedTypeReference<List<Availability>>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().isEmpty());
        assertTrue(responseEntity.getBody().stream().anyMatch(availability -> availability.getFromDate().equals(availabilityDto.getFromDate()) && availability.getToDate().equals(availabilityDto.getToDate())));
    }

    @Test
    @DisplayName("Should Return CONFLICT when Adding Overlapping Accommodation Availability")
    public void testAddOverlappingAccommodationAvailability() {
        long accommodationId = 1L;

        // New availability that overlaps with the existing availability
        AvailabilityDto overlappingAvailabilityDto = new AvailabilityDto(1704585600000L-50000L, 1704585600000L + 25000L, 500.0);

        String url = "/accommodations/" + accommodationId + "/availabilities";

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<AvailabilityDto> request = new HttpEntity<>(overlappingAvailabilityDto, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, request, Void.class);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Return NOT_FOUND when Adding Availability to Non-Existing Accommodation")
    public void testAddFalseAccommodationAvailability() {
        long accommodationId = 99L;
        AvailabilityDto availabilityDto = new AvailabilityDto(1704585600000L+1000L,1704585600000L+90000L,500.0); // Initialize with test data
        String url = "/accommodations/" + accommodationId + "/availabilities";

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<AvailabilityDto> request = new HttpEntity<>(availabilityDto, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, request, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Return FORBIDDEN when Adding Accommodation Availability without Authentication")
    public void testAddAccommodationAvailabilityWithoutAuthentication() {
        long accommodationId = 1L;
        AvailabilityDto availabilityDto = new AvailabilityDto(1704585600000L+1000L,1704585600000L+90000L,500.0); // Initialize with test data
        String url = "/accommodations/" + accommodationId + "/availabilities";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<AvailabilityDto> request = new HttpEntity<>(availabilityDto, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, request, Void.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Remove Accommodation Availability Successfully")
    public void testRemoveAccommodationAvailabilitySuccess() {
        long accommodationId = 1L;
        long availabilityId = 4L;
        String url = "/accommodations/" + accommodationId + "/availabilities/" + availabilityId;

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<MessageDto> responseEntity = restTemplate.exchange(
                url, HttpMethod.DELETE, request, new ParameterizedTypeReference<MessageDto>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Availability removed", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    @DisplayName("Should Return NOT_FOUND when Removing Non-Existing Accommodation Availability")
    public void testRemoveFalseAccommodationAvailability() {
        long accommodationId = 1L;
        long availabilityId = 99L;
        String url = "/accommodations/" + accommodationId + "/availabilities/" + availabilityId;

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        String errorMessage = null;
        try {
            errorMessage = parseResponseEntity(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Availability not found", errorMessage);
    }

    @Test
    @DisplayName("Should Return BAD_REQUEST when Removing Accommodation Availability with Active Reservations")
    public void testRemoveAccommodationAvailabilityWithActiveReservations() {
        long accommodationId = 1L;
        long availabilityId = 1L;
        String url = "/accommodations/" + accommodationId + "/availabilities/" + availabilityId;

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        String errorMessage = null;
        try {
            errorMessage = parseResponseEntity(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Cannot remove availability as there are active reservations in this period.", errorMessage);
    }

    @Test
    @DisplayName("Should Return NOT_FOUND when Removing Accommodation Availability from Non-Existing Accommodation")
    public void testRemoveAccommodationAvailabilityFromFalseAccommodation() {
        long accommodationId = 99L;
        long availabilityId = 1L;
        String url = "/accommodations/" + accommodationId + "/availabilities/" + availabilityId;

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        String errorMessage = null;
        try {
            errorMessage = parseResponseEntity(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Accommodation not found", errorMessage);
    }

    @Test
    @DisplayName("Should Return FORBIDDEN when Removing Accommodation Availability without Authentication")
    public void testRemoveAccommodationAvailabilityWithoutAuthentication() {
        long accommodationId = 1L;
        long availabilityId = 1L;
        String url = "/accommodations/" + accommodationId + "/availabilities/" + availabilityId;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Update Accommodation Booking Details Successfully")
    public void testUpdateAccommodationBookingDetailsSuccess() {
        long accommodationId = 1L;
        String url = "/accommodations/" + accommodationId + "/booking-details";

        AccommodationBookingDetailsDto bookingDetailsDto = new AccommodationBookingDetailsDto();
        bookingDetailsDto.setCancellationDeadline(2);
        bookingDetailsDto.setPricingType(PricingType.PER_GUEST);

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<AccommodationBookingDetailsDto> request = new HttpEntity<>(bookingDetailsDto, headers);

        ResponseEntity<AccommodationBookingDetailsDto> responseEntity = restTemplate.exchange(
                url, HttpMethod.PUT, request, new ParameterizedTypeReference<AccommodationBookingDetailsDto>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(bookingDetailsDto.getCancellationDeadline(), responseEntity.getBody().getCancellationDeadline());
        assertEquals(bookingDetailsDto.getPricingType(), responseEntity.getBody().getPricingType());
    }

    @Test
    @DisplayName("Should Return NOT_FOUND when Updating Booking Details of Non-Existing Accommodation")
    public void testUpdateBookingDetailsOfFalseAccommodation() {
        long accommodationId = 99L;
        String url = "/accommodations/" + accommodationId + "/booking-details";

        AccommodationBookingDetailsDto bookingDetailsDto = new AccommodationBookingDetailsDto();
        bookingDetailsDto.setCancellationDeadline(2);
        bookingDetailsDto.setPricingType(PricingType.PER_GUEST);

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<AccommodationBookingDetailsDto> request = new HttpEntity<>(bookingDetailsDto, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        String errorMessage = null;
        try {
            errorMessage = parseResponseEntity(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Accommodation not found", errorMessage);
    }

    @Test
    @DisplayName("Should Return FORBIDDEN when Updating Booking Details without Authentication")
    public void testUpdateBookingDetailsWithoutAuthentication() {
        long accommodationId = 1L;
        String url = "/accommodations/" + accommodationId + "/booking-details";

        AccommodationBookingDetailsDto bookingDetailsDto = new AccommodationBookingDetailsDto();
        bookingDetailsDto.setCancellationDeadline(2);
        bookingDetailsDto.setPricingType(PricingType.PER_GUEST);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<AccommodationBookingDetailsDto> request = new HttpEntity<>(bookingDetailsDto, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Return NOT_FOUND when Getting Booking Details of Non-Existing Accommodation")
    public void testGetBookingDetailsOfFalseAccommodation() {
        long accommodationId = 99L;
        String url = "/accommodations/" + accommodationId + "/booking-details";

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        String errorMessage = null;
        try {
            errorMessage = parseResponseEntity(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Accommodation not found", errorMessage);
    }

    @Test
    @DisplayName("Should Return FORBIDDEN when Getting Booking Details without Authentication")
    public void testGetBookingDetailsWithoutAuthentication() {
        long accommodationId = 1L;
        String url = "/accommodations/" + accommodationId + "/booking-details";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Return OK when Getting Booking Details of Existing Accommodation")
    public void testGetBookingDetailsOfExistingAccommodation() {
        long accommodationId = 1L;
        String url = "/accommodations/" + accommodationId + "/booking-details";

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<AccommodationBookingDetailsDto> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, request, new ParameterizedTypeReference<AccommodationBookingDetailsDto>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    private String parseResponseEntity(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(body);
        return responseJson.get("message").asText();
    }

    private HttpHeaders createHttpHeaders() {
        String token = obtainAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private String obtainAuthToken() {
        String loginUrl = "/sessions";

        // Create the request body with login credentials
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setEmail("john.doe@example.com");
        credentialsDto.setPassword("123");

        // Create the request entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CredentialsDto> request = new HttpEntity<>(credentialsDto, headers);

        // Send the request to the login endpoint
        ResponseEntity<LoginResponseDto> response = restTemplate.postForEntity(loginUrl, request, LoginResponseDto.class);

        // Extract the token from the response
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getJwt();
        } else {
            throw new IllegalStateException("Failed to obtain authentication token");
        }
    }

}
