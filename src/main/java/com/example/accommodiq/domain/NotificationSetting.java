package com.example.accommodiq.domain;

import com.example.accommodiq.enums.NotificationType;
import jakarta.persistence.*;

@Entity
public class NotificationSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private NotificationType type;

    private boolean isOn;

    public NotificationSetting(Long id, NotificationType type, boolean isOn) {
        this.id = id;
        this.type = type;
        this.isOn = isOn;
    }

    public NotificationSetting() {
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
