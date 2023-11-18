package com.example.accommodiq.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;

@Entity
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private int numberOfRooms;

    // Reference to the owner (User)
    @ManyToOne(cascade = {})
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "apartment")
    private Collection<Reservation> reservations;


    public Apartment() {
    }

    public Apartment(String address, int numberOfRooms, User owner) {
        this.address = address;
        this.numberOfRooms = numberOfRooms;
        this.owner = owner;
    }

    // Other getters and setters...

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
}
