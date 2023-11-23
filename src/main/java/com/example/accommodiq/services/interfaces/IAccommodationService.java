package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.AccommodationReportDto;

import java.util.Collection;

public interface IAccommodationService {
    public Collection<AccommodationListDto> findAll();

    public AccommodationDetailsDto findById(Long accommodationId);

    public Accommodation acceptIncomingChanges(Long accommodationId);

    public AccommodationReportDto getAccommodationReport(Long accommodationId);
}
