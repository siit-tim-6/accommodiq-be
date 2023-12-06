package com.example.accommodiq.dtos;

public class NotificationRequestDto {
    private String text;

    public NotificationRequestDto() {
        super();
    }

    public NotificationRequestDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
