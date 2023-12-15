package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IAccommodationService {
    Collection<AccommodationListDto> findAll();

    Accommodation insert(Host host, AccommodationCreateDto accommodationDto);

    Accommodation update(Accommodation accommodation);

    Collection<AccommodationListDto> findByFilter(String title, String location, Long availableFrom, Long availableTo, Integer priceFrom, Integer priceTo, Integer guests, String type, Set<String> benefits);

    Accommodation changeAccommodationStatus(Long accommodationId, AccommodationStatusDto statusDto);

    AccommodationDetailsDto findById(Long accommodationId);

    Accommodation findAccommodation(Long accommodationId);

    Accommodation updateAccommodation(AccommodationUpdateDto updateDto);

    ResponseEntity<List<Availability>> addAccommodationAvailability(Long accommodationId, AvailabilityDto availabilityDto);

    ResponseEntity<AccommodationBookingDetailsDto> updateAccommodationBookingDetails(Long accommodationId, AccommodationBookingDetailsDto availabilityDto);

    MessageDto removeAccommodationAvailability(Long accommodationId, Long availabilityId);

    AccommodationReportDto getAccommodationReport(Long accommodationId);

    Collection<Review> getAccommodationReviews(Long accommodationId);

    Accommodation addReview(Long accommodationId, ReviewRequestDto reviewDto);

    Collection<AccommodationWithStatusDto> getPendingAccommodations();

    ResponseEntity<AccommodationBookingDetailFormDto> getAccommodationBookingDetails(Long accommodationId);
}
