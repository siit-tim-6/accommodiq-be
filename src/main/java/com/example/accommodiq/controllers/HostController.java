package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.dtos.HostReservationDto;
import com.example.accommodiq.services.interfaces.IHostService;
import com.example.accommodiq.services.interfaces.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/hosts")
public class HostController {
    final private IHostService hostService;
    final private IReviewService reviewService;

    @Autowired
    public HostController(IHostService hostService, IReviewService reviewService) {
        this.hostService = hostService;
        this.reviewService = reviewService;
    }

    @GetMapping
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
    public Host updateHost(@RequestBody Host host) {
        return hostService.update(host);
    }

    @DeleteMapping("/{hostId}")
    public Host deleteHost(@PathVariable Long hostId) {
        return hostService.delete(hostId);
    }

    @DeleteMapping
    public void deleteAll() {
        hostService.deleteAll();
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

    @PostMapping("{hostId}/reviews")
    public ResponseEntity<String> addReview(@PathVariable Long hostId, @RequestBody Review review) {
        reviewService.insert(hostId, review);
        return ResponseEntity.ok("Review has been added.");
    }

    @GetMapping("{hostId}/reviews")
    public Collection<Review> getHostReviews(@PathVariable Long hostId) {
        return reviewService.getHostReviews(hostId);
    }
}
