package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Location;
import com.example.accommodiq.enums.PricingType;

import java.util.List;
import java.util.Set;

public class AccommodationCardDto {
    private Long id;
    private String title;
    private String image;
    private Double rating;
    private int reviewCount;
    private Location location;
    private double minPrice;
    private int minGuests;
    private int maxGuests;
    private double totalPrice;
    private PricingType pricingType;
    private String type;
    private Set<String> benefits;

    public AccommodationCardDto() {
        super();
    }

    public AccommodationCardDto(Long id, String title, String image, Double rating, int reviewCount, Location location,
                                double minPrice, int minGuests, int maxGuests, PricingType pricingType, String type, Set<String> benefits) {
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
        this.pricingType = pricingType;
        this.type = type;
        this.benefits = benefits;
    }

    public AccommodationCardDto(Accommodation accommodation, Long fromDate, Long toDate, Integer guests) {
        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
        this.image = (!accommodation.getImages().isEmpty()) ? accommodation.getImages().get(0) : "";
        this.rating = accommodation.getAverageRating();
        this.reviewCount = accommodation.getReviews().size();
        this.location = accommodation.getLocation();
        this.minPrice = accommodation.getMinPrice();
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.totalPrice = accommodation.getTotalPrice(fromDate, toDate, guests);
        this.pricingType = accommodation.getPricingType();
        this.type = accommodation.getType();
        this.benefits = accommodation.getBenefits();
    }

    public AccommodationCardDto(Accommodation accommodation) {
        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
        this.image = (!accommodation.getImages().isEmpty()) ? accommodation.getImages().get(0) : "";
        this.rating = accommodation.getAverageRating();
        this.reviewCount = accommodation.getReviews().size();
        this.location = accommodation.getLocation();
        this.minPrice = accommodation.getMinPrice();
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.totalPrice = 0;
        this.pricingType = accommodation.getPricingType();
        this.type = accommodation.getType();
        this.benefits = accommodation.getBenefits();
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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

    public PricingType getPricingType() {
        return pricingType;
    }

    public void setPricingType(PricingType pricingType) {
        this.pricingType = pricingType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(Set<String> benefits) {
        this.benefits = benefits;
    }
}
