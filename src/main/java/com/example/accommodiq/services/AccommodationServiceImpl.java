package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import com.example.accommodiq.utilities.AvailabilityConverter;
import com.example.accommodiq.utilities.ReportUtils;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

@Service
public class AccommodationServiceImpl implements IAccommodationService {
    private final static int DEFAULT_CANCELLATION_DEADLINE_VALUE = 1; // in days
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
    public Accommodation insert(Host host, AccommodationCreateDto accommodationDto) {
        Accommodation accommodation = new Accommodation(accommodationDto);
        accommodation.setHost(host);
        accommodation.setCancellationDeadline(DEFAULT_CANCELLATION_DEADLINE_VALUE);
        try {
            accommodationRepository.save(accommodation);
            accommodationRepository.flush();
            return accommodation;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error: " + ex.getMessage());
        }
    }

    @Override
    public Accommodation update(Accommodation accommodation) {
        try {
            findAccommodation(accommodation.getId()); // this will throw ResponseStatusException if accommodation is not found
            accommodationRepository.save(accommodation);
            accommodationRepository.flush();
            return accommodation;
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data integrity violation");
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
    }

    public Accommodation changeAccommodationStatus(Long accommodationId, AccommodationStatusDto statusDto) {
        if (accommodationId == 4L) {
            ReportUtils.throwNotFound("accommodationNotFound");
        }

        return new Accommodation(1L,
                "Cozy Cottage",
                "A charming place to relax",
                "Green Valley",
                null,
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
                add(new AccommodationDetailsReviewDto("John Doe", "Great place!", 4.5, null));
            }

            {
                add(new AccommodationDetailsReviewDto("Jane Smith", "Excellent stay!", 5.0, null));
            }
        };

        return new AccommodationDetailsDto(
                1L,
                "Cozy Cottage",
                4.8,
                25,
                "123 Main St, Cityville",
                detailsHostDto,
                null,
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
                null,
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
    @Transactional
    public Accommodation updateAccommodationAvailability(Long accommodationId, AccommodationBookingDetailsDto accommodationBookingDetailsDto) {
        Accommodation accommodation = findAccommodation(accommodationId);
        Set<Availability> availabilities = AvailabilityConverter.convertToEntities(accommodationBookingDetailsDto.getAvailable());
        accommodation.setAvailable(availabilities);
        accommodation.setCancellationDeadline(accommodationBookingDetailsDto.getCancellationDeadline());
        accommodation.setPricingType(accommodationBookingDetailsDto.getPricingType());
        return update(accommodation);
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
                null,
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
