package com.example.accommodiq.services.interfaces.users;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.ReservationStatus;

import java.util.ArrayList;
import java.util.Collection;

public interface IHostService {

    Collection<Host> getAll();

    Host findHost(Long hostId);

    Host insert(Host host);

    Host update(Host host);

    Host delete(Long hostId);

    void deleteAll();

    Collection<AccommodationCardWithStatusDto> getHostAccommodations();

    Collection<HostReservationCardDto> getHostAccommodationReservationsByFilter(String title, Long startDate, Long endDate, ReservationStatus status);

    ArrayList<FinancialReportEntryDto> getFinancialReport(long fromDate, long toDate);

    Collection<ReviewDto> getHostReviews(Long hostId);

    AccommodationDetailsDto createAccommodation(AccommodationModifyDto accommodationDto);

    ReviewDto addReview(Long hostId, ReviewRequestDto reviewDto);

    AccommodationCardDto deleteAccommodation(Long accommodationId);
}
