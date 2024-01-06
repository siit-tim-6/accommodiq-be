package com.example.accommodiq.services.impl.accommodations;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.AccommodationStatus;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.accommodations.IAccommodationService;
import com.example.accommodiq.specifications.AccommodationSpecification;
import com.example.accommodiq.utilities.ErrorUtils;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

import static com.example.accommodiq.utilities.ErrorUtils.generateNotFound;


@Service
public class AccommodationServiceImpl implements IAccommodationService {
    private final static int DEFAULT_CANCELLATION_DEADLINE_VALUE_IN_DAYS = 1;
    AccommodationRepository accommodationRepository;
    ReservationRepository reservationRepository;

    @Autowired
    public AccommodationServiceImpl(AccommodationRepository accommodationRepository, ReservationRepository reservationRepository) {
        this.accommodationRepository = accommodationRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Accommodation insert(Host host, AccommodationModifyDto accommodationDto) {
        Accommodation accommodation = new Accommodation(accommodationDto);
        accommodation.setHost(host);
        accommodation.setCancellationDeadline(DEFAULT_CANCELLATION_DEADLINE_VALUE_IN_DAYS);
        accommodation.setStatus(AccommodationStatus.PENDING);
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
            findAccommodation(accommodation.getId());
            accommodationRepository.save(accommodation);
            accommodationRepository.flush();
            return accommodation;
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data integrity violation");
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Accommodation not found");
        }
    }

    @Override
    @Transactional
    public Collection<AccommodationCardDto> findByFilter(String title, String location, Long availableFrom, Long availableTo, Integer priceFrom, Integer priceTo, Integer guests, String type, Set<String> benefits) {
        List<Accommodation> searchedAccommodations = accommodationRepository.findAll(AccommodationSpecification.searchAndFilter(title, location, guests, type, benefits)).stream().filter(accommodation -> !accommodation.getAvailable().isEmpty()).toList();
        boolean dateRangeSpecified = availableFrom != null && availableTo != null;
        boolean priceRangeSpecified = priceFrom != null && priceTo != null;

        if (dateRangeSpecified) {
            searchedAccommodations = searchedAccommodations.stream().filter(accommodation -> accommodation.isAvailable(availableFrom, availableTo)).toList();
        }

        List<AccommodationCardDto> accommodations;
        if (!dateRangeSpecified && priceRangeSpecified) {
            accommodations = searchedAccommodations.stream()
                    .map(AccommodationCardDto::new)
                    .filter(accommodationListDto -> accommodationListDto.getMinPrice() != 0 && accommodationListDto.getMinPrice() >= priceFrom && accommodationListDto.getMinPrice() <= priceTo)
                    .toList();
        } else if (dateRangeSpecified && priceRangeSpecified) {
            accommodations = searchedAccommodations.stream()
                    .map(accommodation -> new AccommodationCardDto(accommodation, availableFrom, availableTo, guests))
                    .filter(accommodationListDto -> accommodationListDto.getTotalPrice() >= priceFrom && accommodationListDto.getTotalPrice() <= priceTo)
                    .toList();
        } else if (dateRangeSpecified) {
            accommodations = searchedAccommodations.stream()
                    .map(accommodation -> new AccommodationCardDto(accommodation, availableFrom, availableTo, guests))
                    .toList();
        } else {
            accommodations = searchedAccommodations.stream()
                    .map(AccommodationCardDto::new)
                    .toList();
        }

        return accommodations;
    }

    @Override
    @Transactional
    public AccommodationCardWithStatusDto changeAccommodationStatus(Long accommodationId, AccommodationStatusDto statusDto) {
        Accommodation accommodation = findAccommodation(accommodationId);
        accommodation.setStatus(statusDto.getStatus());
        update(accommodation);
        return new AccommodationCardWithStatusDto(accommodation);
    }

    @Override
    @Transactional
    public AccommodationDetailsDto findById(Long accommodationId) {
        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);

        if (accommodation.isEmpty()) {
            throw ErrorUtils.generateNotFound("accommodationNotFound");
        }

        return new AccommodationDetailsDto(accommodation.get());
    }

    @Override
    public Accommodation findAccommodation(Long accommodationId) {
        Optional<Accommodation> found = accommodationRepository.findById(accommodationId);
        if (found.isEmpty()) {
            throw generateNotFound("accommodationNotFound");
        }
        return found.get();
    }

    @Override
    @Transactional
    public AccommodationCardDto updateAccommodation(AccommodationModifyDto updateDto) {
        Accommodation accommodation = findAccommodation(updateDto.getId());
        accommodation.applyChanges(updateDto);
        update(accommodation);
        return new AccommodationCardDto(accommodation);
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
    public ResponseEntity<AccommodationBookingDetailsDto> updateAccommodationBookingDetails(Long accommodationId, AccommodationBookingDetailsDto accommodationBookingDetailsDto) {
        Accommodation accommodation = findAccommodation(accommodationId);
        accommodation.setCancellationDeadline(accommodationBookingDetailsDto.getCancellationDeadline());
        accommodation.setPricingType(accommodationBookingDetailsDto.getPricingType());
        Accommodation updatedAccommodation = update(accommodation);
        return ResponseEntity.ok(new AccommodationBookingDetailsDto(updatedAccommodation));
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
        ArrayList<AccommodationReportRevenueDto> revenues = new ArrayList<>() {
            {
                add(new AccommodationReportRevenueDto("January", 8000));
            }

            {
                add(new AccommodationReportRevenueDto("February", 8000));
            }
        };

        return new AccommodationReportDto(30, revenues);
    }

    @Override
    public Collection<Review> getAccommodationReviews(Long accommodationId) { // mocked
        return new ArrayList<>() {
            {
                add(new Review(1L, 5, "Great place!", Instant.now().toEpochMilli(), null));
            }

            {
                add(new Review(2L, 5, "Excellent stay!", Instant.now().toEpochMilli(), null));
            }
        };
    }

    @Override
    public Accommodation addReview(Long accommodationId, ReviewRequestDto reviewDto) { // mocked
        if (accommodationId == 4L) {
            throw generateNotFound("accommodationNotFound");
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
    public Collection<AccommodationCardWithStatusDto> getPendingAccommodations() {
        return accommodationRepository.findAllByStatus(AccommodationStatus.PENDING).stream().map(AccommodationCardWithStatusDto::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AccommodationBookingDetailsWithAvailabilityDto> getAccommodationBookingDetails(Long accommodationId) {
        Accommodation accommodation = findAccommodation(accommodationId);
        AccommodationBookingDetailsWithAvailabilityDto accommodationDetails = new AccommodationBookingDetailsWithAvailabilityDto(accommodation);
        return ResponseEntity.ok(accommodationDetails);
    }

    @Override
    public AccommodationPriceDto getTotalPrice(long accommodationId, long dateFrom, long dateTo, int guests) {
        return new AccommodationPriceDto(findAccommodation(accommodationId).getTotalPrice(dateFrom, dateTo, guests));
    }

    @Override
    public Collection<Accommodation> findAccommodationsByHostId(Long hostId) {
        return accommodationRepository.findByHostId(hostId);
    }

    public AccommodationAvailabilityDto getIsAvailable(long accommodationId, long dateFrom, long dateTo) {
        return new AccommodationAvailabilityDto(findAccommodation(accommodationId).isAvailable(dateFrom, dateTo));
    }

    @Override
    @Transactional
    public AccommodationModifyDto getAdvancedDetails(Long accommodationId) {
        Accommodation accommodation = findAccommodation(accommodationId);
        return new AccommodationModifyDto(accommodation);
    }

    @Override
    public AccommodationCardDto deleteAccommodation(Long accommodationId) {
        Accommodation accommodation = findAccommodation(accommodationId);
        reservationRepository.deleteByAccommodationId(accommodationId);
        accommodationRepository.delete(accommodation);
        accommodationRepository.flush();
        return new AccommodationCardDto(accommodation);
    }

    @Override
    public void deleteAllByHostId(Long accountId) {
        accommodationRepository.deleteAllByHostId(accountId);
        accommodationRepository.flush();
    }

    private boolean hasActiveReservations(Accommodation accommodation, Availability availability) {

        Long count = reservationRepository.countOverlappingReservations(
                accommodation.getId(),
                availability.getFromDate(),
                availability.getToDate()
        );
        return count != null && count > 0;
    }

    private boolean isOverlapping(Availability existing, Availability newAvailability) {
        long existingStart = existing.getFromDate();
        long existingEnd = existing.getToDate();
        long newStart = newAvailability.getFromDate();
        long newEnd = newAvailability.getToDate();

        return newStart < existingEnd && newEnd > existingStart;
    }
}
