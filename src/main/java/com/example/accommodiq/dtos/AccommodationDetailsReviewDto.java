package com.example.accommodiq.dtos;

import java.util.Date;

public class AccommodationListReviewDto {
    private String author;
    private String comment;
    private double rating;
    private Date date;

    public AccommodationListReviewDto(String author, String comment, double rating, Date date) {
        this.author = author;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
