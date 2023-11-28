package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Collection<AccommodationListDto> getAllAccommodations(@RequestParam(required = false) String location, @RequestParam(required = false) long availableFrom, @RequestParam(required = false) long availableTo,
                                                                 @RequestParam(required = false) int priceFrom, @RequestParam(required = false) int priceTo, @RequestParam(required = false) int guests, @RequestParam(required = false) List<String> benefits) {
        return accommodationService.findAll();
    }

    @PutMapping("/{accommodationId}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptAccommodationChange(@PathVariable Long accommodationId) {
    }

    @PutMapping("/{accommodationId}/deny")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void declineAccommodationChange(@PathVariable Long accommodationId) {
    }

    @GetMapping("/{accommodationId}")
    public AccommodationDetailsDto getAccommodationDetails(@PathVariable Long accommodationId) {
        return accommodationService.findById(accommodationId);
    }

    @PutMapping("/{accommodationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccommodation(@PathVariable Long accommodationId, @RequestBody AccommodationUpdateDto body) {
    }

    @PostMapping("/{accommodationId}/availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAccommodationAvailability(@PathVariable Long accommodationId, @RequestBody AvailabilityDto body) {
    }

    @DeleteMapping("/{accommodationId}/availability/{availabilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAccommodationAvailability(@PathVariable Long accommodationId, @PathVariable Long availabilityId) {
    }

    @GetMapping("/{accommodationId}/report")
    public AccommodationReportDto getAccommodationReport(@PathVariable Long accommodationId) {
        return accommodationService.getAccommodationReport(accommodationId);
    }
}
