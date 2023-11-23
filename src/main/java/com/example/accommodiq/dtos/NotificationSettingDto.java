package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;

public class NotificationSettingDto {
    private Long id;
    private NotificationSetting.NotificationType type;
    private boolean isOn;

    public NotificationSettingDto(NotificationSetting notificationSetting) {
        this.id = notificationSetting.getId();
        this.type = notificationSetting.getType();
        this.isOn = notificationSetting.isOn();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationSetting.NotificationType getType() {
        return type;
    }

    public void setType(NotificationSetting.NotificationType type) {
        this.type = type;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
