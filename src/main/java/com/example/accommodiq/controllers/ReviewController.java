package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.dtos.ReviewStatusDto;
import com.example.accommodiq.services.interfaces.IReviewService;
import com.example.accommodiq.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    final IReviewService reviewService;
    final IUserService userService;

    @Autowired
    public ReviewController(IReviewService reviewService, IUserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping
    public Collection<Review> getReviews() {
        return reviewService.getAll();
    }

    @PutMapping
    public ReviewDto updateReview(@RequestBody ReviewDto reviewDto) {
        Review existingReview = reviewService.findReview(reviewDto.getId());

        existingReview.setRating(reviewDto.getRating());
        existingReview.setComment(reviewDto.getComment());
        existingReview.setDate(reviewDto.getDate());
        existingReview.setStatus(reviewDto.getStatus());
        existingReview.setGuest(userService.findUser(reviewDto.getGuestId()));

        Review updatedReview = reviewService.update(existingReview);
        return new ReviewDto(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public Review deleteReview(@PathVariable Long reviewId) {
        return reviewService.delete(reviewId);
    }

    @DeleteMapping
    public void deleteAll() {
        reviewService.deleteAll();
    }

    @PutMapping("/{reviewId}/status")
    public ResponseEntity<String> acceptReview(@PathVariable Long reviewId, @RequestBody ReviewStatusDto body) {
        reviewService.setReviewStatus(reviewId, body);
        return ResponseEntity.ok("Review with ID " + reviewId + " status changed.");
    }
}
