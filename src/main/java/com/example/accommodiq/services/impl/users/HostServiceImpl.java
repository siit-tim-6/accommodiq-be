package com.example.accommodiq.services.impl.users;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.NotificationType;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.HostRepository;
import com.example.accommodiq.services.interfaces.accommodations.IAccommodationService;
import com.example.accommodiq.services.interfaces.notifications.INotificationService;
import com.example.accommodiq.services.interfaces.users.IHostService;
import com.example.accommodiq.utilities.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
public class HostServiceImpl implements IHostService {

    final private IAccommodationService accommodationService;

    final private HostRepository hostRepository;

    final
    AccommodationRepository allAccommodations;

    final private INotificationService notificationService;

    @Autowired
    public HostServiceImpl(IAccommodationService accommodationService, HostRepository hostRepository, AccommodationRepository allAccommodations, INotificationService notificationService) {
        this.accommodationService = accommodationService;
        this.hostRepository = hostRepository;
        this.allAccommodations = allAccommodations;
        this.notificationService = notificationService;
    }

    @Override
    public Collection<Host> getAll() {
        return null;
    }

    @Override
    public Host findHost(Long hostId) {
        Optional<Host> found = hostRepository.findById(hostId);
        if (found.isEmpty()) {
            throw ErrorUtils.generateNotFound("hostNotFound");
        }
        return found.get();
    }

    @Override
    public Host insert(Host host) {
        return null;
    }

    @Override
    public Host update(Host host) {
        return null;
    }

    @Override
    public Host delete(Long hostId) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    @Transactional
    public Collection<AccommodationCardWithStatusDto> getHostAccommodations(Long hostId) {
        return allAccommodations.findByHostId(hostId).stream().map(AccommodationCardWithStatusDto::new).toList();
    }

    @Override
    public ArrayList<HostReservationDto> getHostAccommodationReservations(Long hostId) { // mocked
        if (hostId == 4L) {
            throw ErrorUtils.generateNotFound("hostNotFound");
        }

        ArrayList<HostReservationDto> reservations = new ArrayList<>();

        reservations.add(new HostReservationDto(1L, "Cozy Cabin", "John Doe", new Date(), new Date()));
        reservations.add(new HostReservationDto(2L, "Sunny Apartment", "Jane Smith", new Date(), new Date()));

        return reservations;
    }

    @Override
    public ArrayList<FinancialReportEntryDto> getFinancialReport(Long hostId, long fromDate, long toDate) {
        if (hostId == 4L) {
            throw ErrorUtils.generateNotFound("hostNotFound");
        }

        ArrayList<FinancialReportEntryDto> financialReportEntries = new ArrayList<>();

        financialReportEntries.add(new FinancialReportEntryDto("Cozy Cabin", 1200.0, 5));
        financialReportEntries.add(new FinancialReportEntryDto("Sunny Apartment", 1800.0, 8));
        financialReportEntries.add(new FinancialReportEntryDto("Mountain Lodge", 2500.0, 10));

        return financialReportEntries;
    }

    @Override
    public Collection<Review> getHostReviews(Long hostId) { // mocked
        if (hostId == 4L) {
            throw ErrorUtils.generateNotFound("hostNotFound");
        }

        return new ArrayList<>() {
            {
                add(new Review(1L, 5, "Great place!", Instant.now().toEpochMilli(), ReviewStatus.ACCEPTED));
            }
            {
                add(new Review(2L, 5, "Excellent stay!", Instant.now().toEpochMilli(), ReviewStatus.ACCEPTED));
            }
        };
    }

    @Override
    @Transactional
    public AccommodationDetailsDto createAccommodation(Long hostId, AccommodationModifyDto accommodationDto) {
        Host host = findHost(hostId);
        accommodationDto.setId(null);
        Accommodation accommodation = accommodationService.insert(host, accommodationDto);
        accommodation.setPricingType(PricingType.PER_NIGHT);
        return new AccommodationDetailsDto(accommodation);
    }

    @Override
    public Review addReview(User user, ReviewRequestDto reviewDto) { // mocked
        Notification n = new Notification("You have a new rate!", NotificationType.HOST_RATING, user);
        notificationService.createAndSendNotification(n);
        return new Review(1L, 5, "Great place!", new Date().getTime(), ReviewStatus.ACCEPTED);
    }

    @Override
    public AccommodationCardDto deleteAccommodation(Long accommodationId) {
        return accommodationService.deleteAccommodation(accommodationId);
    }
}
