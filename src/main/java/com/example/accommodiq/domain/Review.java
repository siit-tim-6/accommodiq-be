package com.example.accommodiq.domain;

import com.example.accommodiq.enums.ReviewStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    private int rating;
    private String comment;
    private Date date;
    private ReviewStatus status;

    public Review(Long id, int rating, String comment, Date date, ReviewStatus status) {
        super();
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.status = status;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }
}
