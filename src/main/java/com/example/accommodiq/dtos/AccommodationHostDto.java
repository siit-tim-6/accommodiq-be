package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.enums.AccommodationStatus;

import java.util.Random;

public class AccommodationHostDto {
    private Long id;
    private String title;
    private String image;
    private Double rating;
    private int reviewCount;
    private String location;
    private double minPrice;
    private int minGuests;
    private int maxGuests;
    private AccommodationStatus status;

    public AccommodationHostDto() {
        super();
    }

    public AccommodationHostDto(Accommodation accommodation) {
        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
        this.image = accommodation.getImage();
        this.rating = new Random().nextDouble(2, 5);
        this.reviewCount = accommodation.getReviews().size();
        this.location = accommodation.getLocation();
        this.minPrice = new Random().nextInt(300, 600);
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.status = accommodation.getStatus();
    }

    public AccommodationHostDto(Long id, String title, String image, Double rating,
                                int reviewCount, String location, double minPrice, int minGuests, int maxGuests, AccommodationStatus status) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.location = location;
        this.minPrice = minPrice;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.status = status;
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

    public AccommodationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatus status) {
        this.status = status;
    }
}
