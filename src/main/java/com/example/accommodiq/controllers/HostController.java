package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.ReservationStatus;
import com.example.accommodiq.services.interfaces.users.IHostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/hosts")
@CrossOrigin
public class HostController {
    final private IHostService hostService;

    @Autowired
    public HostController(IHostService hostService) {
        this.hostService = hostService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all hosts")
    public Collection<Host> getAllHosts() {
        return hostService.getAll();
    }

    @GetMapping("/reservations")
    @PreAuthorize("hasAuthority('HOST')")
    @Operation(summary = "Get host reservations")
    public Collection<ReservationCardDto> getHostAccommodationReservations(@RequestParam(required = false) String title, @RequestParam(required = false) Long startDate,
                                                                           @RequestParam(required = false) Long endDate, @RequestParam(required = false) ReservationStatus status) {
        return hostService.getHostAccommodationReservationsByFilter(title, startDate, endDate, status);
    }

    @GetMapping("/{hostId}")
    @Operation(summary = "Get host by id")
    public Host getHost(@Parameter(description = "Id of host to get data") @PathVariable Long hostId) {
        return hostService.findHost(hostId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create host")
    public Host createNewHost(@RequestBody Host host) {
        return hostService.insert(host);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('HOST')")
    @Operation(summary = "Update host")
    public Host updateHost(@RequestBody Host host) {
        return hostService.update(host);
    }

    @DeleteMapping("/{hostId}")
    @PreAuthorize("hasAuthority('HOST')")
    @Operation(summary = "Delete host")
    public Host deleteHost(@Parameter(description = "Id of host to be deleted") @PathVariable Long hostId) {
        return hostService.delete(hostId);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete all hosts")
    public void deleteAll() {
        hostService.deleteAll();
    }

    @PostMapping("/accommodations")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('HOST')")
    @Operation(summary = "Create accommodation")
    public AccommodationDetailsDto createNewAccommodation(@RequestBody AccommodationModifyDto accommodation) {
        return hostService.createAccommodation(accommodation);
    }

    @GetMapping("/accommodations")
    @PreAuthorize("hasAuthority('HOST')")
    @Operation(summary = "Get host accommodations")
    public Collection<AccommodationCardWithStatusDto> getHostAccommodations() {
        return hostService.getHostAccommodations();
    }

    @GetMapping("{hostId}/financial-report")
    @PreAuthorize("hasAuthority('HOST')")
    @Operation(summary = "Get host financial report")
    public Collection<FinancialReportEntryDto> getFinancialReport(@PathVariable Long hostId, @RequestParam long fromDate, @RequestParam long toDate) {
        return hostService.getFinancialReport(hostId, fromDate, toDate);
    }

    @PostMapping("{hostId}/reviews")
    @PreAuthorize("hasAuthority('GUEST')")
    @Operation(summary = "Add review")
    public ReviewDto addReview(@PathVariable Long hostId, @RequestBody ReviewRequestDto reviewDto) {
        return hostService.addReview(hostId, reviewDto);
    }

    @GetMapping("{hostId}/reviews")
    @Operation(summary = "Get host reviews that are not declined")
    public Collection<ReviewDto> getHostReviews(@PathVariable Long hostId) {
        return hostService.getHostReviews(hostId);
    }

    @DeleteMapping("accommodations/{accommodationId}")
    @Operation(summary = "Delete accommodation")
    public AccommodationCardDto deleteAccommodation(@PathVariable Long accommodationId) {
        return hostService.deleteAccommodation(accommodationId);
    }
}
