package com.example.accommodiq.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private int numberOfRooms;
    private boolean accepted;
    private String title;
    private String description;
    private String location;
    private int minGuest;
    private int maxGuest;
    private String type;
    private boolean isPricePerGuest;
    private int cancellationDeadline;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User owner;

    public Apartment(Long id, String address, int numberOfRooms, boolean accepted, String title, String description, String location, int minGuest, int maxGuest, String type, boolean isPricePerGuest, int cancellationDeadline, User owner) {
        this.id = id;
        this.address = address;
        this.numberOfRooms = numberOfRooms;
        this.accepted = accepted;
        this.title = title;
        this.description = description;
        this.location = location;
        this.minGuest = minGuest;
        this.maxGuest = maxGuest;
        this.type = type;
        this.isPricePerGuest = isPricePerGuest;
        this.cancellationDeadline = cancellationDeadline;
        this.owner = owner;
    }

    public Apartment() {
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

    public int getMinGuest() {
        return minGuest;
    }

    public void setMinGuest(int minGuest) {
        this.minGuest = minGuest;
    }

    public int getMaxGuest() {
        return maxGuest;
    }

    public void setMaxGuest(int maxGuest) {
        this.maxGuest = maxGuest;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPricePerGuest() {
        return isPricePerGuest;
    }

    public void setPricePerGuest(boolean pricePerGuest) {
        isPricePerGuest = pricePerGuest;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
