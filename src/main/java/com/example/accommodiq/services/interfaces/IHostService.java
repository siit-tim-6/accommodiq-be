package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;

import java.util.ArrayList;
import java.util.Collection;

public interface IHostService {

    Collection<Host> getAll();

    Host findHost(Long hostId);

    Host insert(Host host);

    Host update(Host host);

    Host delete(Long hostId);

    void deleteAll();

    Collection<AccommodationWithStatusDto> getHostAccommodations(Long hostId);

    ArrayList<HostReservationDto> getHostAccommodationReservations(Long hostId);

    ArrayList<FinancialReportEntryDto> getFinancialReport(Long hostId, long fromDate, long toDate);

    Collection<Review> getHostReviews(Long hostId);

    AccommodationDetailsDto createAccommodation(Long hostId, AccommodationCreateDto accommodationDto);

    Review addReview(Long hostId, Long guestId, ReviewRequestDto reviewDto);

    Review addReview(Long hostId, ReviewRequestDto reviewDto);

    AccommodationListDto deleteAccommodation(Long accommodationId);
}
