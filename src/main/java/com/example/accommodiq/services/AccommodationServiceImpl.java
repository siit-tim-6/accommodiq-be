package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.AccommodationStatus;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import com.example.accommodiq.specifications.AccommodationSpecification;
import com.example.accommodiq.utilities.ReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import static com.example.accommodiq.utilities.ReportUtils.throwNotFound;


@Service
public class AccommodationServiceImpl implements IAccommodationService {
    private final static int DEFAULT_CANCELLATION_DEADLINE_VALUE = 1; // in days
    AccommodationRepository accommodationRepository;
    ReservationRepository reservationRepository;
    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
    public AccommodationServiceImpl(AccommodationRepository accommodationRepository, ReservationRepository reservationRepository) {
        this.accommodationRepository = accommodationRepository;
        this.reservationRepository = reservationRepository;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Accommodation not found");
        }
    }

    public Collection<AccommodationListDto> findByFilter(String title, String location, Long availableFrom, Long availableTo, Integer priceFrom, Integer priceTo, Integer guests, String type, Set<String> benefits) {
        List<Accommodation> searchedAccommodations = accommodationRepository.findAll(AccommodationSpecification.searchAndFilter(title, location, guests, type, benefits)).stream().filter(accommodation -> !accommodation.getAvailable().isEmpty()).toList();
        boolean dateRangeSpecified = availableFrom != null && availableTo != null;
        boolean priceRangeSpecified = priceFrom != null && priceTo != null;

        if (dateRangeSpecified) {
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
            throwNotFound("accommodationNotFound");
        }

        return new Accommodation(1L,
                "Cozy Cottage",
                "A charming place to relax",
                "Green Valley",
                null,
                2,
                4,
                "Cottage",
                statusDto.getStatus(),
                PricingType.PER_GUEST,
                true,
                7,
                null
        );
    }

    @Override
    public AccommodationDetailsDto findById(Long accommodationId) {
        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);

        if (accommodation.isEmpty()) {
            ReportUtils.throwNotFound("accommodationNotFound");
        }

        return new AccommodationDetailsDto(accommodation.get());
    }

    @Override
    public Accommodation findAccommodation(Long accommodationId) {
        Optional<Accommodation> found = accommodationRepository.findById(accommodationId);
        if (found.isEmpty()) {
            throwNotFound("accommodationNotFound");
        }
        return found.get();
    }

    @Override
    public Accommodation updateAccommodation(AccommodationUpdateDto updateDto) {
        if (updateDto.getId() == 4L) {
            throwNotFound("accommodationNotFound");
        }

        return new Accommodation(1L,
                "Cozy Cottage",
                "A charming place to relax",
                "Green Valley",
                null,
                2,
                4,
                "Cottage",
                AccommodationStatus.ACCEPTED,
                PricingType.PER_GUEST,
                true,
                7,
                null
        );
    }

    @Override
    @Transactional
    public ResponseEntity<AccommodationBookingDetailsDto> updateAccommodationBookingDetails(Long accommodationId, AccommodationBookingDetailsDto accommodationBookingDetailsDto) {
        Accommodation accommodation = findAccommodation(accommodationId);
        accommodation.setCancellationDeadline(accommodationBookingDetailsDto.getCancellationDeadline());
        accommodation.setPricingType(accommodationBookingDetailsDto.getPricingType());
        Accommodation updatedAccommodation = update(accommodation);
        return ResponseEntity.ok(new AccommodationBookingDetailsDto(updatedAccommodation));
    }

    @Override
    @Transactional
    public ResponseEntity<List<Availability>> addAccommodationAvailability(Long accommodationId, AvailabilityDto availabilityDto) {
        Accommodation accommodation = findAccommodation(accommodationId);
        Availability newAvailability = new Availability(availabilityDto);

        for (Availability existingAvailability : accommodation.getAvailable()) {
            if (isOverlapping(existingAvailability, newAvailability)) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .build();
            }
        }

        accommodation.getAvailable().add(newAvailability);
        Accommodation updatedAccommodation = update(accommodation);

        return ResponseEntity
                .ok(updatedAccommodation.getAvailable().stream().toList());
    }

    @Override
    @Transactional
    public MessageDto removeAccommodationAvailability(Long accommodationId, Long availabilityId) {
        Accommodation accommodation = findAccommodation(accommodationId);
        Optional<Availability> availability = accommodation.getAvailable().stream().filter(a -> a.getId().equals(availabilityId)).findFirst();
        if (availability.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Availability not found");
        }

        Availability availabilityToRemove = availability.get();

        if (hasActiveReservations(accommodation, availabilityToRemove)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot remove availability as there are active reservations in this period.");
        }

        accommodation.getAvailable().remove(availability.get());
        update(accommodation);

        return new MessageDto("Availability removed");
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
            throwNotFound("accommodationNotFound");
        }

        return new Accommodation(1L,
                "Cozy Cottage",
                "A charming place to relax",
                "Green Valley",
                null,
                2,
                4,
                "Cottage",
                AccommodationStatus.ACCEPTED,
                PricingType.PER_GUEST,
                true,
                7,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AccommodationBookingDetailFormDto> getAccommodationBookingDetails(Long accommodationId) {
        Accommodation accommodation = findAccommodation(accommodationId);
        AccommodationBookingDetailFormDto accommodationDetails = new AccommodationBookingDetailFormDto(accommodation);
        return ResponseEntity.ok(accommodationDetails);
    }

    private boolean isOverlapping(Availability existing, Availability newAvailability) {
        long existingStart = existing.getFromDate();
        long existingEnd = existing.getToDate();
        long newStart = newAvailability.getFromDate();
        long newEnd = newAvailability.getToDate();

        return newStart < existingEnd && newEnd > existingStart;
    }

    private boolean hasActiveReservations(Accommodation accommodation, Availability availability) {

        Long count = reservationRepository.countOverlappingReservations(
                accommodation.getId(),
                availability.getFromDate(),
                availability.getToDate()
        );
        return count != null && count > 0;
    }
}
