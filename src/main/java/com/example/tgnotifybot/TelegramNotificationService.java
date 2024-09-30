package com.example.tgnotifybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashSet;
import java.util.Set;

@Service
public class TelegramNotificationService extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramNotificationService.class);

    @Value("${telegram.token}")
    private String botToken;

    @Value("${telegram.bot-name}")
    private String botName;

    @Value("${telegram.user.password}")
    private String tgPassword;

    private Set<Long> users = new HashSet<>();


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String text = message.getText();

            if (!users.contains(chatId)) {
                if (text.equals(tgPassword)) {
                    users.add(chatId);
                    sendMessage(chatId, "Authentication success.");
                } else {
                    sendMessage(chatId, "Authentication failed.");
                }
            } else {
                sendMessage(chatId, "You are already logged in.");
            }
        }

    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.info("Error sending text: ", e.getMessage());
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendNotification(String notification) {
        LOGGER.info("Notification: ", notification);
        users.forEach(userChat -> sendMessage(userChat, notification));
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
