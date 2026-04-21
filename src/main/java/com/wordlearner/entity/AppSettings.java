package com.wordlearner.entity;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table
public class AppSettings {
    @Id
    private Long id;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column
    private LocalTime notificationTime;

    @Column
    private LocalDateTime lastNotifiedAt;

    @Column
    private Long telegramChatId;

    public AppSettings() {
        this.id = 1L;
        this.enabled = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalTime getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(LocalTime notificationTime) {
        this.notificationTime = notificationTime;
    }

    public LocalDateTime getLastNotifiedAt() {
        return lastNotifiedAt;
    }

    public void setLastNotifiedAt(LocalDateTime lastNotifiedAt) {
        this.lastNotifiedAt = lastNotifiedAt;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }
}