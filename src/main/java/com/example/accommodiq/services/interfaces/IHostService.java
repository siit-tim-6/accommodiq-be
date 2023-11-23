package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.dtos.HostReservationDto;

import java.util.ArrayList;

public interface IHostService {
    public ArrayList<AccommodationListDto> getHostAccommodations(Long hostId);

    public ArrayList<HostReservationDto> getHostAccommodationReservations(Long hostId);

    public ArrayList<FinancialReportEntryDto> getFinancialReport(Long hostId, long fromDate, long toDate);
}
