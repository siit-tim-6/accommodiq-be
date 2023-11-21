package com.example.accommodiq.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Notification {
    @Id
    private Long id;

    private String text;

    private long time;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Notification(Long id, String text, long time, User user) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.user = user;
    }

    public Notification() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
