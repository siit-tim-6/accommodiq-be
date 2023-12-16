package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.domain.Review;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

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
    private double totalPrice;

    public AccommodationListDto() {
        super();
    }

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
        this.totalPrice = 0;
    }

    public AccommodationListDto(Accommodation accommodation, Long fromDate, Long toDate) {
        OptionalDouble averageRating = accommodation.getReviews().stream().mapToDouble(Review::getRating).average();
        OptionalDouble minPrice = accommodation.getAvailable().stream().mapToDouble(Availability::getPrice).min();

        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
        this.image = accommodation.getImages().get(0);
        this.rating = averageRating.isPresent() ? averageRating.getAsDouble() : 0;
        this.reviewCount = accommodation.getReviews().size();
        this.location = accommodation.getLocation();
        this.minPrice = minPrice.isPresent() ? minPrice.getAsDouble() : 0;
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.totalPrice = accommodation.getTotalPrice(fromDate, toDate);
    }

    public AccommodationListDto(Accommodation accommodation) {
        OptionalDouble averageRating = accommodation.getReviews().stream().mapToDouble(Review::getRating).average();
        OptionalDouble minPrice = accommodation.getAvailable().stream().mapToDouble(Availability::getPrice).min();

        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
        this.image = accommodation.getImages().isEmpty() ? null : accommodation.getImages().get(0);
        this.rating = averageRating.isPresent() ? averageRating.getAsDouble() : 0;
        this.reviewCount = accommodation.getReviews().size();
        this.location = accommodation.getLocation();
        this.minPrice = minPrice.isPresent() ? minPrice.getAsDouble() : 0;
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.totalPrice = 0;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
