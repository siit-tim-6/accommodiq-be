package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
}
