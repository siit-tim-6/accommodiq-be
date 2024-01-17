package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.domain.Location;
import com.example.accommodiq.enums.PricingType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccommodationModifyDto {
    private Long id;
    private String title;
    private String description;
    private Location location;
    private int minGuests;
    private int maxGuests;
    private PricingType pricingType;
    private boolean automaticAcceptance;
    private List<String> images;
    private String type;
    private Set<String> benefits;

    public AccommodationModifyDto() {
        super();
    }

    public AccommodationModifyDto(Accommodation accommodation) {
        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
        this.description = accommodation.getDescription();
        this.location = accommodation.getLocation();
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.pricingType = accommodation.getPricingType();
        this.automaticAcceptance = accommodation.isAutomaticAcceptance();
        this.images = accommodation.getImages();
        this.type = accommodation.getType();
        this.benefits = accommodation.getBenefits();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public PricingType getPricingType() {
        return pricingType;
    }

    public void setPricingType(PricingType pricingType) {
        this.pricingType = pricingType;
    }

    public boolean isAutomaticAcceptance() {
        return automaticAcceptance;
    }

    public void setAutomaticAcceptance(boolean automaticAcceptance) {
        this.automaticAcceptance = automaticAcceptance;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
