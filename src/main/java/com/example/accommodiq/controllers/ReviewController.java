package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.ReviewStatusDto;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.services.interfaces.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    final IReviewService service;

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

    @PutMapping("/{reviewId}/status")
    public ResponseEntity<String> acceptReview(@PathVariable Long reviewId, @RequestBody ReviewStatusDto body) {
        service.setReviewStatus(reviewId, body);
        return ResponseEntity.ok("Review with ID " + reviewId + " status changed.");
    }
}
