package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.dtos.HostReservationDto;
import com.example.accommodiq.services.interfaces.IHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/hosts")
public class HostController {
    final private IHostService hostService;

    @Autowired
    public HostController(IHostService hostService) {
        this.hostService = hostService;
    }

    @PostMapping("/{hostId}/accommodations")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewAccommodation(@PathVariable Long hostId, @RequestBody AccommodationCreateDto accommodation) {

    }

    @GetMapping("/{hostId}/accommodations")
    public Collection<AccommodationListDto> getHostAccommodations(@PathVariable Long hostId) {
        return hostService.getHostAccommodations(hostId);
    }

    @GetMapping("/{hostId}/reservations")
    public Collection<HostReservationDto> getHostAccommodationReservations(@PathVariable Long hostId) {
        return hostService.getHostAccommodationReservations(hostId);
    }

    @GetMapping("{hostId}/financial-report")
    public Collection<FinancialReportEntryDto> getFinancialReport(@PathVariable Long hostId, @RequestParam long fromDate, @RequestParam long toDate) {
        return hostService.getFinancialReport(hostId, fromDate, toDate);
    }
}
