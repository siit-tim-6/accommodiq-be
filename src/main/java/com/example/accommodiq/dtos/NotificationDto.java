package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.enums.NotificationType;

public class NotificationDto {
    private Long id;
    private String text;
    private Long time;
    private NotificationType type;
    private boolean seen;

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.text = notification.getText();
        this.time = notification.getTime();
        this.type = notification.getType();
        this.seen = notification.isSeen();
    }

    public NotificationDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public NotificationType getType() {
        return type;
    }

    public boolean isSeen() {
        return seen;
    }
}
