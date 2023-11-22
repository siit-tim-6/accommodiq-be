package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Notification;

public class NotificationDto {
    private Long id;
    private String text;
    private Long time;

    public NotificationDto(Long id, String text, Long time) {
        this.id = id;
        this.text = text;
        this.time = time;
    }

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.text = notification.getText();
        this.time = notification.getTime();
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
}
