package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;

import java.util.ArrayList;
import java.util.List;

import com.example.accommodiq.domain.Location;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.enums.ReviewStatus;

import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

public class AccommodationDetailsDto {
    private Long id;
    private String title;
    private double rating;
    private int reviewCount;
    private Location location;
    private AccommodationDetailsHostDto host;
    private List<String> images;
    private int minGuests;
    private int maxGuests;
    private String description;
    private List<ReviewDto> reviews;
    private Set<String> benefits;
    private String type;
    private PricingType pricingType;
    private double minPrice;

    public AccommodationDetailsDto() {
        super();
    }

    public AccommodationDetailsDto(Long id, String title, double rating, int reviewCount, Location location, AccommodationDetailsHostDto host, List<String> images,
                                   int minGuests, int maxGuests, String description, ArrayList<ReviewDto> reviews, Set<String> benefits, String type,
                                   PricingType pricingType, double minPrice) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.location = location;
        this.host = host;
        this.images = images;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.description = description;
        this.reviews = reviews;
        this.benefits = benefits;
        this.type = type;
        this.pricingType = pricingType;
        this.minPrice = minPrice;
    }

    public AccommodationDetailsDto(Accommodation accommodation) {
        OptionalDouble averageRating = accommodation.getReviews().stream()
                .filter(this::isReviewAcceptedOrReported)
                .mapToDouble(Review::getRating).average();

        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
        this.rating = averageRating.isPresent() ? averageRating.getAsDouble() : 0;
        this.reviewCount = accommodation.getReviews().stream()
                .filter(this::isReviewAcceptedOrReported)
                .toList().size();
        this.location = accommodation.getLocation();
        this.host = new AccommodationDetailsHostDto(accommodation.getHost());
        this.images = accommodation.getImages();
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.description = accommodation.getDescription();
        this.reviews = accommodation.getReviews().stream()
                .filter(this::isReviewAcceptedOrReported)
                .map(ReviewDto::new)
                .collect(Collectors.toList());
        this.benefits = accommodation.getBenefits();
        this.type = accommodation.getType();
        this.pricingType = accommodation.getPricingType();
        this.minPrice = accommodation.getMinPrice();
    }

    public AccommodationDetailsDto(Accommodation accommodation, Long loggedInId) {
        OptionalDouble averageRating = accommodation.getReviews().stream()
                .filter(this::isReviewAcceptedOrReported)
                .mapToDouble(Review::getRating).average();

        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
        this.rating = averageRating.isPresent() ? averageRating.getAsDouble() : 0;
        this.reviewCount = accommodation.getReviews().stream()
                .filter(this::isReviewAcceptedOrReported)
                .toList().size();
        this.location = accommodation.getLocation();
        this.host = new AccommodationDetailsHostDto(accommodation.getHost());
        this.images = accommodation.getImages();
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.description = accommodation.getDescription();
        this.reviews = accommodation.getReviews().stream()
                .filter(this::isReviewAcceptedOrReported)
                .map(review -> new ReviewDto(review, loggedInId))
                .collect(Collectors.toList());
        this.benefits = accommodation.getBenefits();
        this.type = accommodation.getType();
        this.pricingType = accommodation.getPricingType();
        this.minPrice = accommodation.getMinPrice();
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public AccommodationDetailsHostDto getHost() {
        return host;
    }

    public void setHost(AccommodationDetailsHostDto host) {
        this.host = host;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }

    public Set<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(Set<String> benefits) {
        this.benefits = benefits;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PricingType getPricingType() {
        return pricingType;
    }

    public void setPricingType(PricingType pricingType) {
        this.pricingType = pricingType;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    private boolean isReviewAcceptedOrReported(Review review) {
        return review.getStatus() == ReviewStatus.ACCEPTED || review.getStatus() == ReviewStatus.REPORTED;
    }
}
