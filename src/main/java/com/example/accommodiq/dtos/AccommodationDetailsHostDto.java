package com.example.accommodiq.dtos;

public class AccommodationDetailsHostDto {
    private Long id;
    private String name;
    private double rating;
    private int reviewCount;

    public AccommodationDetailsHostDto(Long id, String name, double rating, int reviewCount) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.reviewCount = reviewCount;
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
