package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.PriceSearch;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import com.example.accommodiq.specifications.AccommodationSpecification;
import com.example.accommodiq.utilities.ReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.*;

@Service
public class AccommodationServiceImpl implements IAccommodationService {
    AccommodationRepository accommodationRepository;
    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
    public AccommodationServiceImpl(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    @Override
    public Collection<AccommodationListDto> findAll() {
        return new ArrayList<AccommodationListDto>() {
            {
                add(new AccommodationListDto(1L, "City Center Apartment", "https://example.image.com", 4.92,
                        202, "Novi Sad", 540, 2, 5));
            }

            {
                add(new AccommodationListDto(2L, "City Center Apartment", "https://example.image.com", 4.92,
                        202, "Novi Sad", 540, 2, 5));
            }
        };
    }

    @Override
    public Collection<AccommodationListDto> findByFilter(String title, String location, Long availableFrom, Long availableTo, Integer priceFrom, Integer priceTo, Integer guests, List<String> benefits) {
        List<Accommodation> searchedAccommodations = accommodationRepository.findAll(AccommodationSpecification.searchAndFilter(title, location, guests));
        boolean dateRangeSpecified = availableFrom != null && availableTo != null;
        boolean priceRangeSpecified = priceFrom != null && priceTo != null;

        if (availableFrom != null && availableTo != null) {
            searchedAccommodations = searchedAccommodations.stream().filter(accommodation -> accommodation.isAvailable(availableFrom, availableTo)).toList();
        }

        List<AccommodationListDto> accommodationListDtos;
        if (!dateRangeSpecified && priceRangeSpecified) {
            accommodationListDtos = searchedAccommodations.stream()
                    .map(AccommodationListDto::new)
                    .filter(accommodationListDto -> accommodationListDto.getMinPrice() != 0 && accommodationListDto.getMinPrice() >= priceFrom && accommodationListDto.getMinPrice() <= priceTo)
                    .toList();
        } else if (dateRangeSpecified && priceRangeSpecified) {
            accommodationListDtos = searchedAccommodations.stream()
                    .map(accommodation -> new AccommodationListDto(accommodation, availableFrom, availableTo))
                    .filter(accommodationListDto -> accommodationListDto.getTotalPrice() >= priceFrom && accommodationListDto.getTotalPrice() <= priceTo)
                    .toList();
        } else if (dateRangeSpecified && !priceRangeSpecified) {
            accommodationListDtos = searchedAccommodations.stream()
                    .map(accommodation -> new AccommodationListDto(accommodation, availableFrom, availableTo))
                    .toList();
        } else {
            accommodationListDtos = searchedAccommodations.stream()
                    .map(AccommodationListDto::new)
                    .toList();
        }

        return accommodationListDtos;
    }

    public Accommodation changeAccommodationStatus(Long accommodationId, AccommodationStatusDto statusDto) {
        if (accommodationId == 4L) {
            ReportUtils.throwNotFound("accommodationNotFound");
        }

        return new Accommodation(1L,
                "Cozy Cottage",
                "A charming place to relax",
                "Green Valley",
                "cottage_image.jpg",
                2,
                4,
                "Cottage",
                true,
                PricingType.PER_GUEST,
                true,
                7,
                null
        );
    }

    @Override
    public AccommodationDetailsDto findById(Long accommodationId) {
        if (accommodationId == 4L) {
            ReportUtils.throwNotFound("accommodationNotFound");
        }

        AccommodationDetailsHostDto detailsHostDto = new AccommodationDetailsHostDto(1L, "John Doe", 4.92, 202);
        ArrayList<Availability> availabilities = new ArrayList<>() {
            {
                add(new Availability(1L, Instant.now().toEpochMilli(), Instant.now().toEpochMilli(), 150.0));

            }

            {
                add(new Availability(2L, Instant.now().toEpochMilli(), Instant.now().toEpochMilli(), 120.0));
            }
        };
        ArrayList<AccommodationDetailsReviewDto> detailsReviewDtos = new ArrayList<>() {
            {
                add(new AccommodationDetailsReviewDto("John Doe", "Great place!", 4.5, new Date()));
            }

            {
                add(new AccommodationDetailsReviewDto("Jane Smith", "Excellent stay!", 5.0, new Date()));
            }
        };

        return new AccommodationDetailsDto(
                1L,
                "Cozy Cottage",
                4.8,
                25,
                "123 Main St, Cityville",
                detailsHostDto,
                "cottage_image.jpg",
                2,
                4,
                availabilities,
                "A charming cottage with a beautiful garden.",
                detailsReviewDtos
        );
    }

    @Override
    public Accommodation findAccommodation(Long accommodationId) {
        return null;
    }

    @Override
    public Accommodation updateAccommodation(AccommodationUpdateDto updateDto) {
        if (updateDto.getId() == 4L) {
            ReportUtils.throwNotFound("accommodationNotFound");
        }

        return new Accommodation(1L,
                "Cozy Cottage",
                "A charming place to relax",
                "Green Valley",
                "cottage_image.jpg",
                2,
                4,
                "Cottage",
                true,
                PricingType.PER_GUEST,
                true,
                7,
                null
        );
    }

    @Override
    public Accommodation addAccommodationAvailability(Long accommodationId, AvailabilityDto availabilityDto) {
        if (accommodationId == 4L) {
            ReportUtils.throwNotFound("accommodationNotFound");
        }

        return new Accommodation(1L,
                "Cozy Cottage",
                "A charming place to relax",
                "Green Valley",
                "cottage_image.jpg",
                2,
                4,
                "Cottage",
                true,
                PricingType.PER_GUEST,
                true,
                7,
                null
        );
    }

    @Override
    public MessageDto removeAccommodationAvailability(Long accommodationId, Long availabilityId) {
        if (accommodationId == 4L) {
            ReportUtils.throwNotFound("accommodationNotFound");
        }

        if (availabilityId == 4L) {
            ReportUtils.throwNotFound("availabilityNotFound");
        }

        return new MessageDto("Availability successfully deleted");
    }

    @Override
    public AccommodationReportDto getAccommodationReport(Long accommodationId) {
        ArrayList<AccommodationReportRevenueDto> revenueDtos = new ArrayList<>() {
            {
                add(new AccommodationReportRevenueDto("January", 8000));
            }

            {
                add(new AccommodationReportRevenueDto("February", 8000));
            }
        };

        return new AccommodationReportDto(30, revenueDtos);
    }

    @Override
    public Collection<Review> getAccommodationReviews(Long accommodationId) {
        return new ArrayList<Review>() {
            {
                add(new Review(1L, 5, "Great place!", Instant.now().toEpochMilli(), null));
            }

            {
                add(new Review(2L, 5, "Excellent stay!", Instant.now().toEpochMilli(), null));
            }
        };

        //Accommodation accommodation = accommodationRepository.findById(accommodationId);
        //return accommodation.getReviews();
    }

    @Override
    public Accommodation addReview(Long accommodationId, ReviewRequestDto reviewDto) {
        if (accommodationId == 4L) {
            ReportUtils.throwNotFound("accommodationNotFound");
        }

        return new Accommodation(1L,
                "Cozy Cottage",
                "A charming place to relax",
                "Green Valley",
                "cottage_image.jpg",
                2,
                4,
                "Cottage",
                true,
                PricingType.PER_GUEST,
                true,
                7,
                null
        );
    }
}
