package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.IHostService;
import com.example.accommodiq.services.interfaces.IImageService;
import com.example.accommodiq.services.interfaces.IReviewService;
import com.example.accommodiq.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hosts")
@CrossOrigin
public class HostController {
    final private IHostService hostService;
    final private IReviewService reviewService;
    final private IUserService userService;
    final private IImageService imagesService;

    @Autowired
    public HostController(IHostService hostService, IReviewService reviewService, IUserService userService, IImageService imagesService) {
        this.hostService = hostService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.imagesService = imagesService;
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
    public AccommodationDetailsDto createNewAccommodation(@PathVariable Long hostId, @RequestBody AccommodationCreateDto accommodation) {
        return hostService.createAccommodation(hostId, accommodation);
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
    public Review addReview(@PathVariable Long hostId, @RequestBody ReviewRequestDto reviewDto) {
        return hostService.addReview(hostId, reviewDto);
    }

    @GetMapping("{hostId}/reviews")
    public Collection<Review> getHostReviews(@PathVariable Long hostId) {
        return reviewService.getHostReviews(hostId);
    }

    @PostMapping("/upload")
    public List<String> uploadImages(@RequestParam("images") List<MultipartFile> images) {
        return imagesService.uploadImages(images);
    }

    private Review convertToReview(ReviewDto reviewDto) {
        User guest = userService.findUser(reviewDto.getGuestId());
        return new Review(reviewDto, guest);
    }
}
