package com.example.accommodiq.services.interfaces.accommodations;

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
    Accommodation insert(Host host, AccommodationModifyDto accommodationDto);

    Accommodation update(Accommodation accommodation);

    Collection<AccommodationCardDto> findByFilter(String title, String location, Long availableFrom, Long availableTo, Integer priceFrom, Integer priceTo, Integer guests, String type, Set<String> benefits);

    AccommodationCardWithStatusDto changeAccommodationStatus(Long accommodationId, AccommodationStatusDto statusDto);

    AccommodationDetailsDto findById(Long accommodationId);

    Accommodation findAccommodation(Long accommodationId);

    AccommodationCardDto updateAccommodation(AccommodationModifyDto updateDto);

    ResponseEntity<List<Availability>> addAccommodationAvailability(Long accommodationId, AvailabilityDto availabilityDto);

    ResponseEntity<AccommodationBookingDetailsDto> updateAccommodationBookingDetails(Long accommodationId, AccommodationBookingDetailsDto availabilityDto);

    MessageDto removeAccommodationAvailability(Long accommodationId, Long availabilityId);

    AccommodationReportDto getAccommodationReport(Long accommodationId);

    Collection<Review> getAccommodationReviews(Long accommodationId);

    Review addReview(Long accommodationId,Long guestId, ReviewRequestDto reviewDto);

    Collection<AccommodationCardWithStatusDto> getPendingAccommodations();

    ResponseEntity<AccommodationBookingDetailsWithAvailabilityDto> getAccommodationBookingDetails(Long accommodationId);

    AccommodationPriceDto getTotalPrice(long accommodationId, long dateFrom, long dateTo, int guests);

    AccommodationAvailabilityDto getIsAvailable(long accommodationId, long dateFrom, long dateTo);

    AccommodationModifyDto getAdvancedDetails(Long accommodationId);

    Collection<Accommodation> findAccommodationsByHostId(Long hostId);

    AccommodationCardDto deleteAccommodation(Long accommodationId);

    void deleteAllByHostId(Long accountId);
}
