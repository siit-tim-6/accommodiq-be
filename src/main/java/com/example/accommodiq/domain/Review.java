package com.example.accommodiq.domain;

import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.enums.ReviewStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    private int rating;
    private String comment;
    private Long date = Instant.now().toEpochMilli();
    private ReviewStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private User guest;


    public Review(Long id, int rating, String comment, Long date, ReviewStatus status) {
        super();
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.status = status;
    }

    public Review(ReviewDto reviewDto, User guest) {
        this.id = reviewDto.getId();
        this.rating = reviewDto.getRating();
        this.comment = reviewDto.getComment();
        this.date = reviewDto.getDate();
        this.status = reviewDto.getStatus();
        this.guest = guest;
    }

    public Review() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public User getGuest() { return guest; }

    public void setGuest(User guest) { this.guest = guest; }
}
