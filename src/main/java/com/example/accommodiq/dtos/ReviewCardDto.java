package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Review;

public class ReviewCardDto {
    private ReviewDto review;
    private Long accommodationId;
    private String image;
    private String title;

    public ReviewCardDto() {
        super();
    }

    public ReviewCardDto(Review review, Accommodation accommodation) {
        this.review = new ReviewDto(review);
        this.accommodationId = accommodation.getId();
        this.image = accommodation.getImages().get(0);
        this.title = accommodation.getTitle();
    }

    public ReviewDto getReview() {
        return review;
    }

    public void setReview(ReviewDto review) {
        this.review = review;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
