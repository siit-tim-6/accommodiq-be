package com.example.accommodiq.domain;

import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.enums.AccommodationStatus;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.utilities.ReportUtils;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.Comparator;
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
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images = new ArrayList<>();
    private int minGuests;
    private int maxGuests;
    private String type;
    private AccommodationStatus status;
    private PricingType pricingType;
    private boolean automaticAcceptance;
    private int cancellationDeadline;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Availability> available = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Host host;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> benefits = new HashSet<>();

    public Accommodation(Long id, String title, String description, String location, List<String> images, int minGuests, int maxGuests, String type, AccommodationStatus status, PricingType pricingType,
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
        this.status = status;
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
        this.images = accommodationDto.getImages();
        this.type = accommodationDto.getType();
        this.benefits = accommodationDto.getBenefits();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AccommodationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatus status) {
        this.status = status;
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

    public double getRating() {
        Hibernate.initialize(reviews);
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0);
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Set<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(Set<String> benefits) {
        this.benefits = benefits;
    }

    public boolean isAvailable(Long from, Long to) {
        Long finalFrom = from;
        Long oneDay = (long) (60 * 60 * 24);

        if (this.available == null) {
            return false;
        }

        List<Availability> availabilityCandidates = this.available.stream().filter(availability ->
                (availability.getFromDate() <= finalFrom && finalFrom <= availability.getToDate())
                        || (availability.getFromDate() <= to && to <= availability.getToDate())
        ).sorted(Comparator.comparing(Availability::getFromDate)).toList();

        if (availabilityCandidates.isEmpty()) {
            return false;
        }

        for (Availability availabilityCandidate : availabilityCandidates) {
            if (availabilityCandidate.getFromDate() <= from && to <= availabilityCandidate.getToDate()) {
                return true;
            } else if (availabilityCandidate.getFromDate() <= from && to > availabilityCandidate.getToDate()) {
                from = availabilityCandidate.getFromDate() + oneDay;
            } else {
                return false;
            }
        }

        return false;
    }

    public double getTotalPrice(Long fromDate, Long toDate, Integer guests) {
        if (!isAvailable(fromDate, toDate)) {
            ReportUtils.throwBadRequest("accommodationUnavailable");
        }

        if (guests > maxGuests || guests < minGuests || (guests == null && pricingType == PricingType.PER_GUEST)) {
            ReportUtils.throwBadRequest("invalidGuestNumber");
        }

        Long oneDay = (long) (60 * 60 * 24);

        List<Availability> availabilityCandidates = available.stream().filter(availability ->
                (availability.getFromDate() <= fromDate && fromDate <= availability.getToDate())
                        || (availability.getFromDate() <= toDate && toDate <= availability.getToDate())
        ).sorted(Comparator.comparing(Availability::getFromDate)).toList();

        Long fromDateCopy = fromDate;
        double totalPrice = 0;
        for (Availability availabilityCandidate : availabilityCandidates) {
            while (availabilityCandidate.getFromDate() <= fromDateCopy && fromDateCopy <= availabilityCandidate.getToDate()) {
                totalPrice += availabilityCandidate.getPrice();
                fromDateCopy += oneDay;

                if (fromDateCopy > toDate) {
                    return (pricingType == PricingType.PER_GUEST) ? totalPrice * guests : totalPrice;
                }
            }
        }

        return (pricingType == PricingType.PER_GUEST) ? totalPrice * guests : totalPrice;
    }
}

