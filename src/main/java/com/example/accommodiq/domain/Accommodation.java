package com.example.accommodiq.domain;

import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.enums.PricingType;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String location;
    @ElementCollection
    private List<String> images = new ArrayList<>();
    private int minGuests;
    private int maxGuests;
    private String type;
    private boolean accepted;
    private PricingType pricingType;
    private boolean automaticAcceptance;
    private int cancellationDeadline;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Availability> available = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Host host;

    public Accommodation(Long id, String title, String description, String location, List<String> images, int minGuests, int maxGuests, String type, boolean accepted, PricingType pricingType,
                         boolean automaticAcceptance, int cancellationDeadline, Host host) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.images = images;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.accepted = accepted;
        this.pricingType = pricingType;
        this.automaticAcceptance = automaticAcceptance;
        this.cancellationDeadline = cancellationDeadline;
        this.host = host;
    }

    public Accommodation(AccommodationCreateDto accommodationDto) {
        this.title = accommodationDto.getTitle();
        this.description = accommodationDto.getDescription();
        this.location = accommodationDto.getLocation();
        this.minGuests = accommodationDto.getMinGuests();
        this.maxGuests = accommodationDto.getMaxGuests();
        this.available = accommodationDto.getAvailable();
        this.pricingType = accommodationDto.getPricingType();
        this.automaticAcceptance = accommodationDto.isAutomaticAcceptance();
    }

    public Accommodation() {
        super();
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

    public List<String> getImage() {
        return images;
    }

    public void setImage(List<String> images) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
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

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Availability> getAvailable() {
        return available;
    }

    public void setAvailable(Set<Availability> available) {
        this.available = available;
    }

    public Host getHost() { return host; }

    public void setHost(Host host) { this.host = host; }

    public double getRating() {
        Hibernate.initialize(reviews);
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0);
    }
}

