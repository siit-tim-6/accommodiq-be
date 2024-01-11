package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.dtos.ReviewStatusDto;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.services.interfaces.feedback.IReviewService;
import com.example.accommodiq.services.interfaces.users.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@CrossOrigin
public class ReviewController {

    final IReviewService reviewService;
    final IUserService userService;

    @Autowired
    public ReviewController(IReviewService reviewService, IUserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PutMapping
    @Operation(summary = "Update review")
    public ReviewDto updateReview(@Parameter(description = "Updated parameter") @RequestBody ReviewDto reviewDto) {
        Review existingReview = reviewService.findReview(reviewDto.getId());

        existingReview.setRating(reviewDto.getRating());
        existingReview.setComment(reviewDto.getComment());
        existingReview.setDate(reviewDto.getDate());
        existingReview.setStatus(reviewDto.getStatus());
        existingReview.setGuest(userService.findUser(reviewDto.getAuthorId()));

        Review updatedReview = reviewService.update(existingReview);
        return new ReviewDto(updatedReview);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete all reviews")
    public void deleteAll() {
        reviewService.deleteAll();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all reviews")
    public Collection<Review> getReviews() {
        return reviewService.getAll();
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GUEST')")
    @Operation(summary = "Delete review")
    public MessageDto deleteReview(@Parameter(description = "Id of review to be deleted") @PathVariable Long reviewId) {
        return reviewService.delete(reviewId);
    }

    @PutMapping("/{reviewId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Change review status")
    public MessageDto setReviewStatus(@Parameter(description = "Id of review to change status")@PathVariable Long reviewId, @RequestBody ReviewStatusDto body) {
        return reviewService.setReviewStatus(reviewId, body.getStatus());
    }

    @PutMapping("/{reviewId}/report")
    @PreAuthorize("hasAuthority('HOST')")
    @Operation(summary = "Report review")
    public MessageDto reportReview(@Parameter(description = "Id of review to report")@PathVariable Long reviewId) {
        return reviewService.setReviewStatus(reviewId, ReviewStatus.REPORTED);
    }
}
