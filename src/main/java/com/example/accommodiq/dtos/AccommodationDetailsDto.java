package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Availability;

import java.util.ArrayList;

public class AccommodationDetailsDto {
    private Long id;
    private String title;
    private double rating;
    private int reviewCount;
    private String address;
    private AccommodationDetailsHostDto host;
    private String image;
    private int minGuests;
    private int maxGuests;
    private ArrayList<Availability> available;
    private String description;
    private ArrayList<AccommodationDetailsReviewDto> reviews;

    public AccommodationDetailsDto(Long id, String title, double rating, int reviewCount, String address, AccommodationDetailsHostDto host, String image,
                                   int minGuests, int maxGuests, ArrayList<Availability> available, String description, ArrayList<AccommodationDetailsReviewDto> reviews) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.address = address;
        this.host = host;
        this.image = image;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.available = available;
        this.description = description;
        this.reviews = reviews;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AccommodationDetailsHostDto getHost() {
        return host;
    }

    public void setHost(AccommodationDetailsHostDto host) {
        this.host = host;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public ArrayList<Availability> getAvailable() {
        return available;
    }

    public void setAvailable(ArrayList<Availability> available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<AccommodationDetailsReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<AccommodationDetailsReviewDto> reviews) {
        this.reviews = reviews;
    }
}