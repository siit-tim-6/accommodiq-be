package com.example.accommodiq.dtos;

import jakarta.validation.constraints.*;

import java.util.Date;

public class ReviewRequestDto {
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be greater than or equal to 1")
    @Max(value = 5, message = "Rating must be less than or equal to 5")
    private int rating;
    @NotBlank(message = "Comment is required")
    @Size(min = 3, max = 255, message = "Comment must be between 3 and 255 characters")
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
