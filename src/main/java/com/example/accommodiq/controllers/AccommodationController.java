package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.accommodations.IAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/accommodations")
@CrossOrigin
public class AccommodationController {
    final private IAccommodationService accommodationService;

    @Autowired
    public AccommodationController(IAccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping()
    public Collection<AccommodationCardDto> getAllAccommodations(@RequestParam(required = false) String title, @RequestParam(required = false) String location, @RequestParam(required = false) Long availableFrom, @RequestParam(required = false) Long availableTo,
                                                                 @RequestParam(required = false) Integer priceFrom, @RequestParam(required = false) Integer priceTo, @RequestParam(required = false) Integer guests, @RequestParam(required = false) String type, @RequestParam(required = false) Set<String> benefits) {
        return accommodationService.findByFilter(title, location, availableFrom, availableTo, priceFrom, priceTo, guests, type, benefits);
    }

    @PutMapping("/{accommodationId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccommodationCardWithStatusDto changeAccommodationStatus(@PathVariable Long accommodationId, @RequestBody AccommodationStatusDto body) {
        return accommodationService.changeAccommodationStatus(accommodationId, body);
    }

    @GetMapping("/{accommodationId}")
    public AccommodationDetailsDto getAccommodationDetails(@PathVariable Long accommodationId) {
        return accommodationService.findById(accommodationId);
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('HOST')")
    public AccommodationCardDto updateAccommodation(@RequestBody AccommodationModifyDto body) {
        return accommodationService.updateAccommodation(body);
    }

    @GetMapping("/{accommodationId}/booking-details")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<AccommodationBookingDetailsWithAvailabilityDto> getAccommodationBookingDetails(@PathVariable Long accommodationId) {
        return accommodationService.getAccommodationBookingDetails(accommodationId);
    }

    @PutMapping("/{accommodationId}/booking-details")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<AccommodationBookingDetailsDto> updateAccommodationBookingDetails(@PathVariable Long accommodationId, @RequestBody AccommodationBookingDetailsDto body) {
        return accommodationService.updateAccommodationBookingDetails(accommodationId, body);
    }

    @PostMapping("/{accommodationId}/availabilities")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<List<Availability>> addAccommodationAvailability(@PathVariable Long accommodationId, @RequestBody AvailabilityDto body) {
        return accommodationService.addAccommodationAvailability(accommodationId, body);
    }

    @DeleteMapping("/{accommodationId}/availabilities/{availabilityId}")
    @PreAuthorize("hasAuthority('HOST')")
    public MessageDto removeAccommodationAvailability(@PathVariable Long accommodationId, @PathVariable Long availabilityId) {
        return accommodationService.removeAccommodationAvailability(accommodationId, availabilityId);
    }

    @GetMapping("/{accommodationId}/financial-report")
    @PreAuthorize("hasAuthority('HOST')")
    public AccommodationReportDto getAccommodationReport(@PathVariable Long accommodationId) {
        return accommodationService.getAccommodationReport(accommodationId);
    }

    @PostMapping("{accommodationId}/reviews")
    @PreAuthorize("hasAuthority('GUEST')")
    public Accommodation addReview(@PathVariable Long accommodationId, @RequestBody ReviewRequestDto reviewDto) {
        return accommodationService.addReview(accommodationId, reviewDto);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<AccommodationCardWithStatusDto> getPendingAccommodations() {
        return accommodationService.getPendingAccommodations();
    }

    @GetMapping("/{accommodationId}/total-price")
    public AccommodationPriceDto getTotalPrice(@PathVariable Long accommodationId, @RequestParam long dateFrom, @RequestParam long dateTo, @RequestParam int guests) {
        return accommodationService.getTotalPrice(accommodationId, dateFrom, dateTo, guests);
    }

    @GetMapping("/{accommodationId}/is-available")
    public AccommodationAvailabilityDto getIsAvailable(@PathVariable Long accommodationId, @RequestParam long dateFrom, @RequestParam long dateTo) {
        return accommodationService.getIsAvailable(accommodationId, dateFrom, dateTo);
    }

    @GetMapping("{accommodationId}/advanced")
    @PreAuthorize("hasAuthority('HOST')")
    public AccommodationModifyDto getAdvancedDetails(@PathVariable Long accommodationId) {
        return accommodationService.getAdvancedDetails(accommodationId);
    }
}
