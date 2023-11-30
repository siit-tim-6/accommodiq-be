package com.example.accommodiq.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    private Long time;
    @ManyToOne()
    private User user;

    public Notification(Long id, String text, long time, User user) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.user = user;
    }

    public Notification() {
    }

    @PrePersist
    public void prePersist() {
        if (time == null) {
            time = Instant.now().getEpochSecond();
        }
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
