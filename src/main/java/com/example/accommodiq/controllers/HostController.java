package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.users.IHostService;
import com.example.accommodiq.services.interfaces.feedback.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/hosts")
@CrossOrigin
public class HostController {
    final private IHostService hostService;
    final private IReviewService reviewService;
    final private IAccountService accountService;

    @Autowired
    public HostController(IHostService hostService, IReviewService reviewService, IAccountService accountService) {
        this.hostService = hostService;
        this.reviewService = reviewService;
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Host> getAllHosts() {
        return hostService.getAll();
    }

    @GetMapping("/{hostId}")
    public Host getHost(@PathVariable Long hostId) {
        return hostService.findHost(hostId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Host createNewHost(@RequestBody Host host) {
        return hostService.insert(host);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('HOST')")
    public Host updateHost(@RequestBody Host host) {
        return hostService.update(host);
    }

    @DeleteMapping("/{hostId}")
    @PreAuthorize("hasAuthority('HOST')")
    public Host deleteHost(@PathVariable Long hostId) {
        return hostService.delete(hostId);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAll() {
        hostService.deleteAll();
    }

    @PostMapping("/accommodations")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('HOST')")
    public AccommodationDetailsDto createNewAccommodation(@RequestBody AccommodationCreateDto accommodation) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long hostId = ((Account) accountService.loadUserByUsername(email)).getId();
        return hostService.createAccommodation(hostId, accommodation);
    }

    @GetMapping("/accommodations")
    @PreAuthorize("hasAuthority('HOST')")
    public Collection<AccommodationWithStatusDto> getHostAccommodations() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long hostId = ((Account) accountService.loadUserByUsername(email)).getId();

        return hostService.getHostAccommodations(hostId);
    }

    @GetMapping("/{hostId}/reservations")
    @PreAuthorize("hasAuthority('HOST')")
    public Collection<HostReservationDto> getHostAccommodationReservations(@PathVariable Long hostId) {
        return hostService.getHostAccommodationReservations(hostId);
    }

    @GetMapping("{hostId}/financial-report")
    @PreAuthorize("hasAuthority('HOST')")
    public Collection<FinancialReportEntryDto> getFinancialReport(@PathVariable Long hostId, @RequestParam long fromDate, @RequestParam long toDate) {
        return hostService.getFinancialReport(hostId, fromDate, toDate);
    }

    @PostMapping("{hostId}/reviews")
    @PreAuthorize("hasAuthority('GUEST')")
    public Review addReview(@PathVariable Long hostId, @RequestBody ReviewRequestDto reviewDto) {
        return hostService.addReview(hostId, reviewDto);
    }

    @GetMapping("{hostId}/reviews")
    public Collection<Review> getHostReviews(@PathVariable Long hostId) {
        return reviewService.getHostReviews(hostId);
    }

    @DeleteMapping("accommodations/{accommodationId}")
    public AccommodationListDto deleteAccommodation(@PathVariable Long accommodationId) {
        return hostService.deleteAccommodation(accommodationId);
    }
}
