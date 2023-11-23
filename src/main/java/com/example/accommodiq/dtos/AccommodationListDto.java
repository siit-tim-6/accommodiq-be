package com.example.accommodiq.dtos;

public class AccommodationListDto {
    private Long id;
    private String title;
    private String image;
    private Double rating;
    private int reviewCount;
    private String location;
    private double minPrice;
    private int minGuests;
    private int maxGuests;

    public AccommodationListDto(Long id, String title, String image, Double rating,
                                int reviewCount, String location, double minPrice, int minGuests, int maxGuests) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.location = location;
        this.minPrice = minPrice;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }
}
