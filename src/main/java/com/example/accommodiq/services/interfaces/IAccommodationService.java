package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface IAccommodationService {
    Collection<AccommodationListDto> findAll();

    Accommodation insert(Host host, AccommodationCreateDto accommodationDto);

    Accommodation update(Accommodation accommodation);

    Accommodation changeAccommodationStatus(Long accommodationId, AccommodationStatusDto statusDto);

    AccommodationDetailsDto findById(Long accommodationId);

    Accommodation findAccommodation(Long accommodationId);

    Accommodation updateAccommodation(AccommodationUpdateDto updateDto);

    ResponseEntity<Accommodation> addAccommodationAvailability(Long accommodationId, AvailabilityDto availabilityDto);

    Accommodation updateAccommodationBookingDetails(Long accommodationId, AccommodationBookingDetailsDto availabilityDto);

    MessageDto removeAccommodationAvailability(Long accommodationId, Long availabilityId);

    AccommodationReportDto getAccommodationReport(Long accommodationId);

    Collection<Review> getAccommodationReviews(Long accommodationId);

    Accommodation addReview(Long accommodationId, ReviewRequestDto reviewDto);
}
