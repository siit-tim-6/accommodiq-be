package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeAccommodationStatus(@PathVariable Long accommodationId, @RequestBody AccommodationStatusDto body) {
    }

    @GetMapping("/{accommodationId}")
    public AccommodationDetailsDto getAccommodationDetails(@PathVariable Long accommodationId) {
        return accommodationService.findById(accommodationId);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccommodation(@RequestBody AccommodationUpdateDto body) {
    }

    @PostMapping("/{accommodationId}/availabilities")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAccommodationAvailability(@PathVariable Long accommodationId, @RequestBody AvailabilityDto body) {
    }

    @DeleteMapping("/{accommodationId}/availabilities/{availabilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAccommodationAvailability(@PathVariable Long accommodationId, @PathVariable Long availabilityId) {
    }

    @GetMapping("/{accommodationId}/financial-report")
    public AccommodationReportDto getAccommodationReport(@PathVariable Long accommodationId) {
        return accommodationService.getAccommodationReport(accommodationId);
    }

    @PostMapping("{accommodationId}/reviews")
    public ResponseEntity<String> addReview(@PathVariable Long accommodationId, @RequestBody Review review) {
        return ResponseEntity.ok("Review has been added.");
    }
}
