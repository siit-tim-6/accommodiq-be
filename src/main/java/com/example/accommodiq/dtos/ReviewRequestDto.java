package com.example.accommodiq.dtos;

import java.util.Date;

public class ReviewRequestDto {
    private int rating;
    private String comment;
    private Date date;

    public ReviewRequestDto() {
        super();
    }

    public ReviewRequestDto(int rating, String comment, Date date) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
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
}
