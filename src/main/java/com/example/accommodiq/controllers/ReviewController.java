package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.services.interfaces.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> acceptReview(@PathVariable Long reviewId) {
        service.setReviewStatus(reviewId, ReviewStatus.ACCEPTED);
        return ResponseEntity.ok("Review with ID " + reviewId + " has been accepted.");
    }

    @PutMapping("/{reviewId}/deny")
    public ResponseEntity<String> denyReview(@PathVariable Long reviewId) {
        service.setReviewStatus(reviewId, ReviewStatus.DECLINED);
        return ResponseEntity.ok("Review with ID " + reviewId + " has been denied.");
    }

    @PutMapping("/{reviewId}/report")
    public ResponseEntity<String> reportReview(@PathVariable Long reviewId) {
        service.setReviewStatus(reviewId, ReviewStatus.REPORTED);
        return ResponseEntity.ok("Review with ID " + reviewId + " has been reported.");
    }

    @PostMapping("/host/{hostId}")
    public ResponseEntity<String> addReview(@PathVariable Long hostId, @RequestBody Review review) {
        service.insert(hostId, review);
        return ResponseEntity.ok("Review has been added.");
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
