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
    private Long authorId;
    private String author;
    private boolean deletable;

    public ReviewDto() {
        super();
    }

    public ReviewDto(Long id, int rating, String comment, Long date, ReviewStatus status, Long guestId, String firstName, String lastName, boolean deletable) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.status = status;
        this.authorId = guestId;
        this.author = firstName + " " + lastName;
        this.deletable = deletable;
    }

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.date = review.getDate();
        this.status = review.getStatus();
        this.authorId = review.getGuest().getId();
        this.author = review.getGuest().getFirstName() + " " + review.getGuest().getLastName();
        this.deletable = false;
    }

    public ReviewDto(Review review, Long loggedUserId) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.date = review.getDate();
        this.status = review.getStatus();
        this.authorId = review.getGuest().getId();
        this.author = review.getGuest().getFirstName() + " " + review.getGuest().getLastName();
        this.deletable = loggedUserId.equals(review.getGuest().getId());
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

    public Long getAuthorId() {
        return authorId;
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

    public void setAuthorId(Long guestId) {
        this.authorId = guestId;
    }

    public Review toReview() {
        return new Review(id, rating, comment, date, status);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
