package com.example.accommodiq.domain;

import com.example.accommodiq.enums.NotificationType;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    private boolean seen;

    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Long getTime() {
        return time;
    }

    private Long time;

    public Notification() {
    }

    @PrePersist
    public void prePersist() {
        if (time == null) {
            time = Instant.now().toEpochMilli();
        }
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public boolean isSeen() {
        return seen;
    }
}
