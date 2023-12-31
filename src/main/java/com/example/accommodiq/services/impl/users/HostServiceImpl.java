package com.example.accommodiq.services.impl.users;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.repositories.HostRepository;
import com.example.accommodiq.repositories.ReservationRepository;
import com.example.accommodiq.services.interfaces.accommodations.IAccommodationService;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.services.interfaces.accommodations.IReservationService;
import com.example.accommodiq.services.interfaces.users.IGuestService;
import com.example.accommodiq.services.interfaces.users.IHostService;
import com.example.accommodiq.utilities.ErrorUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HostServiceImpl implements IHostService {

    final private IAccommodationService accommodationService;

    final private HostRepository hostRepository;

    final private ReservationRepository reservationRepository;

    final private IGuestService guestService;

    final AccommodationRepository allAccommodations;

    @Autowired
    public HostServiceImpl(IAccommodationService accommodationService, HostRepository hostRepository, AccommodationRepository allAccommodations, IGuestService guestService, ReservationRepository reservationRepository) {
        this.accommodationService = accommodationService;
        this.hostRepository = hostRepository;
        this.allAccommodations = allAccommodations;
        this.guestService = guestService;
        this.reservationRepository = reservationRepository;
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
        try {
            hostRepository.save(host);
            hostRepository.flush();
            return new Host(host);
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Host cannot be inserted");
        }
    }

    @Override
    public Host update(Host host) {
        try {
            findHost(host.getId());
            hostRepository.save(host);
            hostRepository.flush();
            return new Host(host);
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException)) {
                e = c;
            }
            if ((c != null)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Host cannot be updated");
            }
            throw ex;
        }
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
    public Collection<Review> getHostReviews(Long hostId) {
        Host host = findHost(hostId);
        return host.getReviews().stream()
                .filter(review -> review.getStatus() != ReviewStatus.DECLINED)
                .collect(Collectors.toList());
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
    public Review addReview(Long hostId, Long guestId, ReviewRequestDto reviewDto) {
        canGuestCommentAndRateHost(guestId, hostId); // this will throw ResponseStatusException if guest cannot comment and rate host
        Host host = findHost(hostId);
        Guest guest = guestService.findGuest(guestId);
        Review review = new Review(reviewDto, guest, ReviewStatus.ACCEPTED);
        host.getReviews().add(review);
        update(host);
        return review;
    }

    @Override
    public AccommodationCardDto deleteAccommodation(Long accommodationId) {
        return accommodationService.deleteAccommodation(accommodationId);
    }

    private void canGuestCommentAndRateHost(Long guestId, Long hostId) {
        Collection<Accommodation> hostAccommodations = accommodationService.findAccommodationsByHostId(hostId);

        List<Long> accommodationIds = hostAccommodations.stream()
                .map(Accommodation::getId)
                .collect(Collectors.toList());

        long currentTime = System.currentTimeMillis()/1000; // current time in seconds
        Collection<Reservation> reservations = reservationRepository
                .findByUserIdAndAccommodationIdInAndStatusNotAndEndDateBefore(guestId, accommodationIds, ReservationStatus.CANCELLED, currentTime);

        if (reservations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Guest cannot comment and rate this host, because he has not stayed in any of his accommodations");
        }
    }
}