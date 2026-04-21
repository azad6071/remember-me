package com.wordlearner.scheduler;

import com.wordlearner.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyNotificationScheduler {

    @Autowired
    private TelegramBotService telegramBotService;

    @Value("${scheduler.test:false}")
    private boolean testMode;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendDailyNotification() {
        telegramBotService.sendDailyNotification();
    }

    @Scheduled(fixedDelay = 60000)
    public void testNotification() {
        if (testMode) {
            System.out.println("=== TEST NOTIFICATION ===");
            telegramBotService.sendDailyNotification();
        }
    }
}