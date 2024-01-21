package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.ReviewStatus;

import java.util.OptionalDouble;

public class AccommodationDetailsHostDto {
    private Long id;
    private String name;
    private double rating;
    private int reviewCount;

    public AccommodationDetailsHostDto() {
        super();
    }

    public AccommodationDetailsHostDto(Long id, String name, double rating, int reviewCount) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public AccommodationDetailsHostDto(Host host) {
        OptionalDouble averageRating = host.getReviews().stream()
                .filter(this::isReviewAcceptedOrReported)
                .mapToDouble(Review::getRating).average();

        this.id = host.getId();
        this.name = host.getFirstName() + " " + host.getLastName();
        this.rating = averageRating.isPresent() ? averageRating.getAsDouble() : 0;
        this.reviewCount = host.getReviews().stream()
                .filter(this::isReviewAcceptedOrReported)
                .toList().size();
    }

    private boolean isReviewAcceptedOrReported(Review review) {
        return review.getStatus() == ReviewStatus.ACCEPTED || review.getStatus() == ReviewStatus.REPORTED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
