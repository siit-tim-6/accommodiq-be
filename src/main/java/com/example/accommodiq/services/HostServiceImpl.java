package com.example.accommodiq.services;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.dtos.HostReservationDto;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.services.interfaces.IHostService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
public class HostServiceImpl implements IHostService {
    @Override
    public Collection<Host> getAll() {
        return null;
    }

    @Override
    public Host findHost(Long hostId) {
        return new Host(1L,"John", "Doe", "123 Main Street", "555-1234",null);
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
    public ArrayList<AccommodationListDto> getHostAccommodations(Long hostId) {
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
    public ArrayList<HostReservationDto> getHostAccommodationReservations(Long hostId) {
        ArrayList<HostReservationDto> reservations = new ArrayList<>();

        reservations.add(new HostReservationDto(1L, "Cozy Cabin", "John Doe", new Date(), new Date()));
        reservations.add(new HostReservationDto(2L, "Sunny Apartment", "Jane Smith", new Date(), new Date()));

        return reservations;
    }

    @Override
    public ArrayList<FinancialReportEntryDto> getFinancialReport(Long hostId, long fromDate, long toDate) {
        ArrayList<FinancialReportEntryDto> financialReportEntries = new ArrayList<>();

        financialReportEntries.add(new FinancialReportEntryDto("Cozy Cabin", 1200.0, 5));
        financialReportEntries.add(new FinancialReportEntryDto("Sunny Apartment", 1800.0, 8));
        financialReportEntries.add(new FinancialReportEntryDto("Mountain Lodge", 2500.0, 10));

        return financialReportEntries;
    }

    @Override
    public Collection<Review> getHostReviews(Long hostId) {
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
}
