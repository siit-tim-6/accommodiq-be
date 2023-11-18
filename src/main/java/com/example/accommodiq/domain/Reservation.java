package com.example.accommodiq.domain;

import jakarta.persistence.*;

import java.util.Date;


@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date startDate;
    private Date endDate;
    private int numberOfGuests;
    private Status status;

    @ManyToOne(cascade = {})
    private User user;

    @ManyToOne(cascade = {})
    private Apartment apartment;

    public enum Status {
        WAITING,
        APPROVED,
        DENIED
    }
}
