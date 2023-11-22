package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.PricingType;

public class AccommodationUpdateDto {
    private String title;
    private String description;
    private String location;
    private int minGuests;
    private int maxGuests;
    private PricingType pricingType;
    private boolean automaticAcceptance;

    public AccommodationUpdateDto(String title, String description, String location, int minGuests,
                                  int maxGuests, PricingType pricingType, boolean automaticAcceptance) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.pricingType = pricingType;
        this.automaticAcceptance = automaticAcceptance;
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
}
