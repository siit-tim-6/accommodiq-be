package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.PriceSearch;

import java.util.Collection;
import java.util.List;

public interface IAccommodationService {
    Collection<AccommodationListDto> findAll();

    Collection<AccommodationListDto> findByFilter(String title, String location, Long availableFrom, Long availableTo, Integer priceFrom, Integer priceTo, PriceSearch priceSearchType, Integer guests, List<String> benefits);

    Accommodation changeAccommodationStatus(Long accommodationId, AccommodationStatusDto statusDto);

    AccommodationDetailsDto findById(Long accommodationId);

    Accommodation findAccommodation(Long accommodationId);

    Accommodation updateAccommodation(AccommodationUpdateDto updateDto);

    Accommodation addAccommodationAvailability(Long accommodationId, AvailabilityDto availabilityDto);

    MessageDto removeAccommodationAvailability(Long accommodationId, Long availabilityId);

    AccommodationReportDto getAccommodationReport(Long accommodationId);

    Collection<Review> getAccommodationReviews(Long accommodationId);

    Accommodation addReview(Long accommodationId, ReviewRequestDto reviewDto);
}
