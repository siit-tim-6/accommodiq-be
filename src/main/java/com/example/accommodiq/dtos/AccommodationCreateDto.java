package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.PricingType;

import java.util.ArrayList;

public class AccommodationCreateDto {
    private String title;
    private String description;
    private String location;
    private int minGuests;
    private int maxGuests;
    private ArrayList<AvailabilityDto> available;
    private PricingType pricingType;
    private boolean automaticAcceptance;
}
