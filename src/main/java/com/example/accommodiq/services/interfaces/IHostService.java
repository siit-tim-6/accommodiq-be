package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.dtos.HostReservationDto;

import java.util.ArrayList;
import java.util.Collection;

public interface IHostService {

    public Collection<Host> getAll();

    public Host findHost(Long hostId);

    public Host insert(Host host);

    public Host update(Host host);

    public Host delete(Long hostId);

    public void deleteAll();

    public ArrayList<AccommodationListDto> getHostAccommodations(Long hostId);

    public ArrayList<HostReservationDto> getHostAccommodationReservations(Long hostId);

    public ArrayList<FinancialReportEntryDto> getFinancialReport(Long hostId, long fromDate, long toDate);

    Collection<Review> getHostReviews(Long hostId);
}
