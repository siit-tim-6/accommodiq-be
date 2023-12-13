package com.example.accommodiq.services;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.services.interfaces.IHostService;
import com.example.accommodiq.utilities.ReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
public class HostServiceImpl implements IHostService {
    final
    AccommodationRepository allAccommodations;

    @Autowired
    public HostServiceImpl(AccommodationRepository allAccommodations) {
        this.allAccommodations = allAccommodations;
    }

    @Override
    public Collection<Host> getAll() {
        return null;
    }

    @Override
    public Host findHost(Long hostId) {
        return new Host(1L, "John", "Doe", "123 Main Street", "555-1234");
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
    public Collection<AccommodationListDto> getHostAccommodations(Long hostId) {
        return allAccommodations.findByHostId(hostId).stream().map(AccommodationListDto::new).toList();
    }

    @Override
    public ArrayList<HostReservationDto> getHostAccommodationReservations(Long hostId) {
        if (hostId == 4L) {
            ReportUtils.throwNotFound("hostNotFound");
        }

        ArrayList<HostReservationDto> reservations = new ArrayList<>();

        reservations.add(new HostReservationDto(1L, "Cozy Cabin", "John Doe", new Date(), new Date()));
        reservations.add(new HostReservationDto(2L, "Sunny Apartment", "Jane Smith", new Date(), new Date()));

        return reservations;
    }

    @Override
    public ArrayList<FinancialReportEntryDto> getFinancialReport(Long hostId, long fromDate, long toDate) {
        if (hostId == 4L) {
            ReportUtils.throwNotFound("hostNotFound");
        }

        ArrayList<FinancialReportEntryDto> financialReportEntries = new ArrayList<>();

        financialReportEntries.add(new FinancialReportEntryDto("Cozy Cabin", 1200.0, 5));
        financialReportEntries.add(new FinancialReportEntryDto("Sunny Apartment", 1800.0, 8));
        financialReportEntries.add(new FinancialReportEntryDto("Mountain Lodge", 2500.0, 10));

        return financialReportEntries;
    }

    @Override
    public Collection<Review> getHostReviews(Long hostId) {
        if (hostId == 4L) {
            ReportUtils.throwNotFound("hostNotFound");
        }

        return new ArrayList<Review>() {
            {
                add(new Review(1L, 5, "Great place!", Instant.now().toEpochMilli(), ReviewStatus.ACCEPTED));
            }

            {
                add(new Review(2L, 5, "Excellent stay!", Instant.now().toEpochMilli(), ReviewStatus.ACCEPTED));
            }
        };

        //Host host = findHost(hostId);
        //return host.getReviews();
    }

    @Override
    public AccommodationDetailsDto createAccommodation(Long hostId, AccommodationCreateDto accommodationDto) {
        if (hostId == 4L) {
            ReportUtils.throwNotFound("hostNotFound");
        }

        AccommodationDetailsHostDto detailsHostDto = new AccommodationDetailsHostDto(1L, "John Doe", 4.92, 202);

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
                "A charming cottage with a beautiful garden.",
                null,
                null
        );
    }

    @Override
    public Review addReview(Long hostId, ReviewRequestDto reviewDto) {
        if (hostId == 4L) {
            ReportUtils.throwNotFound("hostNotFound");
        }

        return new Review(1L, 5, "Great place!", new Date().getTime(), ReviewStatus.ACCEPTED);
    }
}
