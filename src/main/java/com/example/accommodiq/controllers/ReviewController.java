package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.services.interfaces.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    final
    IReviewService service;

    @Autowired
    public ReviewController(IReviewService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<Review> getReviews() {
        return service.getAll();
    }

    @PostMapping
    public Review insertReview(@RequestBody Review review) {
        return service.insert(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) {
        return service.update(review);
    }

    @DeleteMapping("/{reviewId}")
    public Review deleteReview(@PathVariable Long reviewId) {
        return service.delete(reviewId);
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

    @PutMapping("/{reviewId}/accept")
    public void acceptReview(@PathVariable Long reviewId) {
    }

    @PutMapping("/{reviewId}/deny")
    public void denyReview(@PathVariable Long reviewId) {
    }

    @PutMapping("/{reviewId}/report")
    public void reportReview(@PathVariable Long reviewId) {
    }

    @PostMapping("/host/{hostId}")
    public void addReview(@PathVariable Long hostId, @RequestBody Review review) {
    }

    @GetMapping("/host/{hostId}")
    public Collection<Review> getHostReviews(@PathVariable Long hostId) {
        return service.getHostReviews(hostId);
    }

    @GetMapping("/accommodation/{accommodationId}")
    public Collection<Review> getAccommodationReviews(@PathVariable Long accommodationId) {
        return service.getAccommodationReviews(accommodationId);
    }

}
