package com.example.accommodiq.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Host extends User {

    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    public Host() {
        super();
    }

    public Host(Long id, String firstName, String lastName, String address, String phoneNumber) {
        super(id, firstName, lastName, address, phoneNumber);
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

}
