package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.ReviewStatus;

import java.time.Instant;

public class ReviewDto {
    private Long id;
    private int rating;
    private String comment;
    private Long date = Instant.now().toEpochMilli();
    private ReviewStatus status;
    private Long guestId;

    public ReviewDto() {
        super();
    }

    public ReviewDto(Long id, int rating, String comment, Long date, ReviewStatus status, Long guestId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.status = status;
        this.guestId = guestId;
    }

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.date = review.getDate();
        this.status = review.getStatus();
        this.guestId = review.getGuest().getId();
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Long getDate() {
        return date;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public Review toReview() {
        return new Review(id, rating, comment, date, status);
    }
}
