package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.enums.NotificationType;
import jakarta.validation.constraints.NotNull;

public class NotificationSettingDto {
    @NotNull(message = "Notification type is required")
    private NotificationType type;
    private boolean on;

    public NotificationSettingDto() {
        super();
    }

    public NotificationSettingDto(NotificationSetting notificationSetting) {
        this.type = notificationSetting.getType();
        this.on = notificationSetting.isOn();
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
