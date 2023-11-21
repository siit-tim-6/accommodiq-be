package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/accommodations")
public class AccommodationController {
    private IAccommodationService accommodationService;

    @Autowired
    public AccommodationController(IAccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping()
    public Collection<Accommodation> getAllAccommodations() {
        return accommodationService.findAll();
    }

    @PutMapping("/{accommodationId}/accept")
    public void acceptAccommodationChange(@PathVariable Long accommodationId) {
        return;
    }

    @PutMapping("/{accommodationId}/deny")
    public void declineAccommodationChange(@PathVariable Long accommodationId) {
        return;
    }

    @GetMapping("/{accommodationId}")
    public Accommodation getAccommodationDetails(@PathVariable Long accommodationId) {
        return accommodationService.findById(accommodationId);
    }

    @PutMapping("/{accommodationId}")
    public Accommodation updateAccommodation(@PathVariable Long accommodationId) {
        return accommodationService.findById(accommodationId);
    }

    @PostMapping("/{accommodationId}/availability")
    public Accommodation addAccommodationAvailability(@PathVariable Long accommodationId) {
        return null;
    }

    @DeleteMapping("/{accommodationId}/availability/{availabilityId}")
    public Accommodation removeAccommodationAvailability(@PathVariable Long accommodationId, @PathVariable Long availabilityId) {
        return null;
    }

    @GetMapping("/{accommodationId}/report")
    public Accommodation getAccommodationReport(@PathVariable Long accommodationId) {
        return null;
    }
}
