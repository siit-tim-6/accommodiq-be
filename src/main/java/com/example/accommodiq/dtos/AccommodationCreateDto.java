package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.enums.PricingType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccommodationCreateDto {
    private String title;
    private String description;
    private String location;
    private int minGuests;
    private int maxGuests;
    private Set<AvailabilityDto> available;
    private PricingType pricingType;
    private boolean automaticAcceptance;
    private List<String> images;
    private String type;
    private Set<String> benefits = new HashSet<>();

    public AccommodationCreateDto() {
    }

    public AccommodationCreateDto(String title, String description, String location, int minGuests, int maxGuests, Set<AvailabilityDto> available, PricingType pricingType, boolean automaticAcceptance, List<String> images, String type, Set<String> benefits) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.available = available;
        this.pricingType = pricingType;
        this.automaticAcceptance = automaticAcceptance;
        this.images = images;
        this.type = type;
        this.benefits = benefits;
    }

    public AccommodationCreateDto(Accommodation accommodation) {
        this.title = accommodation.getTitle();
        this.description = accommodation.getDescription();
        this.location = accommodation.getLocation();
        this.minGuests = accommodation.getMinGuests();
        this.maxGuests = accommodation.getMaxGuests();
        this.available = new HashSet<>();
        for (Availability availability : accommodation.getAvailable()) {
            this.available.add(new AvailabilityDto(availability.getFromDate(), availability.getToDate(), availability.getPrice()));
        }
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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

    public Set<AvailabilityDto> getAvailableDto() {
        return available;
    }

    public Set<Availability> getAvailable() {
        Set<Availability> available = new HashSet<>();
        for (AvailabilityDto availabilityDto : this.available) {
            available.add(new Availability(availabilityDto));
        }
        return available;
    }

    public void setAvailable(Set<AvailabilityDto> available) {
        this.available = available;
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

    public List<String> getImages() { return images; }

    public void setImages(List<String> images) { this.images = images; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
    public Set<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(Set<String> benefits) {
        this.benefits = benefits;
    }
}
