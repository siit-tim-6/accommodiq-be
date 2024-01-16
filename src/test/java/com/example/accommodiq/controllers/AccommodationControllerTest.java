package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.dtos.AvailabilityDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

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
    public void testAddAccommodationAvailability() {
        long accommodationId = 1L;
        AvailabilityDto availabilityDto = new AvailabilityDto(1704585600000L+1000,1704585600000L+90000,500); // Initialize with test data
        String url = "/accommodations/" + accommodationId + "/availabilities";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AvailabilityDto> request = new HttpEntity<>(availabilityDto, headers);

        ResponseEntity<List<Availability>> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, request, new ParameterizedTypeReference<List<Availability>>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().isEmpty());
    }
}
