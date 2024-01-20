package com.example.accommodiq.services.impl.users;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.*;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.HostRepository;
import com.example.accommodiq.repositories.ReviewRepository;
import com.example.accommodiq.services.interfaces.accommodations.IAccommodationService;
import com.example.accommodiq.services.interfaces.accommodations.IReservationService;
import com.example.accommodiq.services.interfaces.notifications.INotificationService;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.users.IGuestService;
import com.example.accommodiq.services.interfaces.users.IHostService;
import com.example.accommodiq.utilities.ErrorUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    final private IReservationService reservationService;

    final private IGuestService guestService;

    final AccommodationRepository allAccommodations;

    final IAccountService accountService;

    final private INotificationService notificationService;

    final private ReviewRepository reviewRepository;

    @Autowired
    public HostServiceImpl(IAccommodationService accommodationService, HostRepository hostRepository, AccommodationRepository allAccommodations,
                           IGuestService guestService, IReservationService reservationService, IAccountService accountService, INotificationService notificationService, ReviewRepository reviewRepository) {
        this.accommodationService = accommodationService;
        this.hostRepository = hostRepository;
        this.allAccommodations = allAccommodations;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.reviewRepository = reviewRepository;
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
    public Collection<AccommodationCardWithStatusDto> getHostAccommodations() {
        Long hostId = getHostId();
        return allAccommodations.findByHostId(hostId).stream().map(AccommodationCardWithStatusDto::new).toList();
    }

    @Override
    public Collection<AccommodationTitleDto> getHostAccommodationTitles() {
        Long hostId = getHostId();
        return allAccommodations.findByHostId(hostId).stream().map(AccommodationTitleDto::new).toList();
    }

    @Override
    public Collection<HostReservationCardDto> getHostAccommodationReservationsByFilter(String title, Long startDate, Long endDate, ReservationStatus status) {
        if (startDate != null && endDate != null && startDate >= endDate) {
            throw ErrorUtils.generateException(HttpStatus.BAD_REQUEST, "invalidDateRange");
        }

        Long hostId = getHostId();
        return reservationService.findHostReservationsByFilter(hostId, title, startDate, endDate, status).stream().map(reservation -> new HostReservationCardDto(reservation, hostId)).toList();
    }

    @Override
    public List<FinancialReportEntryDto> getFinancialReport(long fromDate, long toDate) {
        if (fromDate >= toDate || toDate > Instant.now().toEpochMilli()) {
            throw ErrorUtils.generateException(HttpStatus.BAD_REQUEST, "invalidDateRange");
        }

        Long hostId = getHostId();
        Map<Long, List<Reservation>> hostPreviousReservations = reservationService
                .findHostReservationsByFilter(hostId, null, fromDate, toDate, ReservationStatus.ACCEPTED)
                .stream().collect(Collectors.groupingBy(reservation -> reservation.getAccommodation().getId()));

        return hostPreviousReservations.values().stream().map(FinancialReportEntryDto::new).toList();
    }

    @Override
    public Collection<ReviewDto> getHostReviews(Long hostId) {
        Long loggedInId = getLoggedInAccountId();
        Host host = findHost(hostId);
        return host.getReviews().stream()
                .filter(review -> review.getStatus() != ReviewStatus.DECLINED)
                .map(review -> new ReviewDto(review, loggedInId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccommodationDetailsDto createAccommodation(AccommodationModifyDto accommodationDto) {
        Long hostId = getHostId();
        Host host = findHost(hostId);
        accommodationDto.setId(null);
        Accommodation accommodation = accommodationService.insert(host, accommodationDto);
        accommodation.setPricingType(PricingType.PER_NIGHT);
        return new AccommodationDetailsDto(accommodation);
    }

    @Override
    public ReviewDto addReview(Long hostId, ReviewRequestDto reviewDto) {
        Long guestId = getGuestId();
        reservationService.validateGuestReviewEligibility(guestId, hostId); // this will throw ResponseStatusException if guest cannot comment and rate host
        Host host = findHost(hostId);
        Guest guest = guestService.findGuest(guestId);
        Review review = new Review(reviewDto, guest, ReviewStatus.ACCEPTED);
        Notification n = new Notification("You've got a new rating!", NotificationType.HOST_RATING, host);
        notificationService.createAndSendNotification(n);
        host.getReviews().add(review);
        update(host);
        return new ReviewDto(review, guestId);
    }

    @Override
    public AccommodationCardDto deleteAccommodation(Long accommodationId) {
        return accommodationService.deleteAccommodation(accommodationId);
    }

    @Override
    public Collection<HostReviewCardDto> getHostReviewsByStatus(ReviewStatus status) {
        List<Review> reviews = reviewRepository.findByStatus(status);
        Collection<Host> hosts = hostRepository.findHostsContainingReviews(reviews.stream().map(Review::getId).toList());
        Collection<HostReviewCardDto> reviewCards = new ArrayList<>();
        for (Host host : hosts) {
            List<Review> hostReviews = host.getReviews().stream().filter(reviews::contains).toList();
            for (Review review : hostReviews)
                reviewCards.add(new HostReviewCardDto(review, host));
        }

        return reviewCards;
    }

    @Override
    public MessageDto changeReviewStatus(Long reviewId, ReviewStatusDto body) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> ErrorUtils.generateNotFound("reviewNotFound"));
        review.setStatus(body.getStatus());
        reviewRepository.save(review);
        reviewRepository.flush();
        return new MessageDto("Review status changed");
    }

    private Long getHostId() {
        Account account = getAccount();
        if (account.getRole() != AccountRole.HOST) throw new RuntimeException("User is not a host");
        return account.getId();
    }

    private Long getGuestId() {
        Account account = getAccount();
        if (account.getRole() != AccountRole.GUEST) throw new RuntimeException("User is not a guest");
        return account.getId();
    }

    private Long getLoggedInAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return -1L;
        }

        String email = authentication.getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        return account.getId();
    }

    private Account getAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return (Account) accountService.loadUserByUsername(email);
    }
}
