package com.example.tgnotifybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg/api/notify")
public class NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final TelegramNotificationService telegramBot;

    public NotificationController(TelegramNotificationService telegramBot) {
        this.telegramBot = telegramBot;
    }

    @GetMapping
    public String notify(@RequestParam("status") String status) {
        telegramBot.sendNotification("Process status: " + status);
        LOGGER.info("Notification status: ", status);
        return "Notification sent with status: " + status + "\n";
    }
}
