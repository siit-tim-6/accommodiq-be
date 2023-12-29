package com.example.accommodiq.dtos;

import java.util.Date;

public class ReviewRequestDto {
    private int rating;
    private String comment;

    public ReviewRequestDto() {
        super();
    }

    public ReviewRequestDto(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
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
}
