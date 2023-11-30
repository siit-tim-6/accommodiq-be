package com.example.accommodiq.dtos;

import java.util.Date;

public class ReviewRequestDto {
    private int rating;
    private String comment;
    private Date date;
    private String userName;

    public ReviewRequestDto() {
        super();
    }

    public ReviewRequestDto(int rating, String comment, Date date, String userName) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
