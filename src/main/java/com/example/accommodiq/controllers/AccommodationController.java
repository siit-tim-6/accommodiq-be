package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/accommodations")
public class AccommodationController {
    final private IAccommodationService accommodationService;

    @Autowired
    public AccommodationController(IAccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping()
    public Collection<AccommodationListDto> getAllAccommodations(@RequestParam(required = false) String location, @RequestParam(required = false) Long availableFrom, @RequestParam(required = false) Long availableTo,
                                                                 @RequestParam(required = false) Integer priceFrom, @RequestParam(required = false) Integer priceTo, @RequestParam(required = false) Integer guests, @RequestParam(required = false) List<String> benefits) {
        return accommodationService.findAll();
    }

    @PutMapping("/{accommodationId}/status")
    public Accommodation changeAccommodationStatus(@PathVariable Long accommodationId, @RequestBody AccommodationStatusDto body) {
        return accommodationService.changeAccommodationStatus(accommodationId, body);
    }

    @GetMapping("/{accommodationId}")
    public AccommodationDetailsDto getAccommodationDetails(@PathVariable Long accommodationId) {
        return accommodationService.findById(accommodationId);
    }

    @PutMapping()
    public Accommodation updateAccommodation(@RequestBody AccommodationUpdateDto body) {
        return accommodationService.updateAccommodation(body);
    }

    @PostMapping("/{accommodationId}/availabilities")
    public Accommodation addAccommodationAvailability(@PathVariable Long accommodationId, @RequestBody AvailabilityDto body) {
        return accommodationService.addAccommodationAvailability(accommodationId, body);
    }

    @DeleteMapping("/{accommodationId}/availabilities/{availabilityId}")
    public MessageDto removeAccommodationAvailability(@PathVariable Long accommodationId, @PathVariable Long availabilityId) {
        return accommodationService.removeAccommodationAvailability(accommodationId, availabilityId);
    }

    @GetMapping("/{accommodationId}/financial-report")
    public AccommodationReportDto getAccommodationReport(@PathVariable Long accommodationId) {
        return accommodationService.getAccommodationReport(accommodationId);
    }

    @PostMapping("{accommodationId}/reviews")
    public Accommodation addReview(@PathVariable Long accommodationId, @RequestBody ReviewRequestDto reviewDto) {
        return accommodationService.addReview(accommodationId, reviewDto);
    }
}
