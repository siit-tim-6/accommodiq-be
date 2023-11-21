package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/accommodations")
public class AccommodationController {
    private IAccommodationService accommodationService;

    @Autowired
    public AccommodationController(IAccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping()
    public Collection<AccommodationListDto> getAllAccommodations() {
        ArrayList<AccommodationListDto> retVal = new ArrayList<>() {
            {
                add(new AccommodationListDto(1L, "City Center Apartment", "https://example.image.com", 4.92,
                        202, "Novi Sad", 540, 2, 5));
            }

            {
                add(new AccommodationListDto(2L, "City Center Apartment", "https://example.image.com", 4.92,
                        202, "Novi Sad", 540, 2, 5));
            }
        };
        System.out.println(retVal.size());

        return retVal;
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
        AccommodationDetailsHostDto detailsHostDto = new AccommodationDetailsHostDto(1L, "John Doe", 4.92, 202);
        ArrayList<Availability> availabilities = new ArrayList<>() {
            {
                add(new Availability(1L, new Date(), new Date(), 150.0));

            }

            {
                add(new Availability(2L, new Date(), new Date(), 120.0));
            }
        };
        ArrayList<AccommodationDetailsReviewDto> detailsReviewDtos = new ArrayList<>() {
            {
                add(new AccommodationDetailsReviewDto("John Doe", "Great place!", 4.5, new Date()));
            }

            {
                add(new AccommodationDetailsReviewDto("Jane Smith", "Excellent stay!", 5.0, new Date()));
            }
        };

        return new AccommodationDetailsDto(
                1L,
                "Cozy Cottage",
                4.8,
                25,
                "123 Main St, Cityville",
                detailsHostDto,
                "cottage_image.jpg",
                2,
                4,
                availabilities,
                "A charming cottage with a beautiful garden.",
                detailsReviewDtos
        );
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
        ArrayList<AccommodationReportRevenueDto> revenueDtos = new ArrayList<>() {
            {
                add(new AccommodationReportRevenueDto("January", 8000));
            }

            {
                add(new AccommodationReportRevenueDto("February", 8000));
            }
        };

        return new AccommodationReportDto(30, revenueDtos);
    }
}
