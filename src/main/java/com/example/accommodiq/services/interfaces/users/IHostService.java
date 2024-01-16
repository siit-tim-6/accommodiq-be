package com.example.accommodiq.services.interfaces.users;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.enums.ReviewStatus;

import java.util.Collection;
import java.util.List;

public interface IHostService {

    Collection<Host> getAll();

    Host findHost(Long hostId);

    Host insert(Host host);

    Host update(Host host);

    Host delete(Long hostId);

    void deleteAll();

    Collection<AccommodationCardWithStatusDto> getHostAccommodations();

    Collection<AccommodationTitleDto> getHostAccommodationTitles();

    Collection<HostReservationCardDto> getHostAccommodationReservationsByFilter(String title, Long startDate, Long endDate, ReservationStatus status);

    List<FinancialReportEntryDto> getFinancialReport(long fromDate, long toDate);

    Collection<ReviewDto> getHostReviews(Long hostId);

    AccommodationDetailsDto createAccommodation(AccommodationModifyDto accommodationDto);

    ReviewDto addReview(Long hostId, ReviewRequestDto reviewDto);

    AccommodationCardDto deleteAccommodation(Long accommodationId);

    Collection<HostReviewCardDto> getHostReviewsByStatus(ReviewStatus status);
}
