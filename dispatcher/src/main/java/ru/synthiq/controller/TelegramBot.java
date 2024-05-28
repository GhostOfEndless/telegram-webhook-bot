package ru.synthiq.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.webhook.TelegramWebhookBot;
import org.telegram.telegrambots.webhook.starter.TelegramBotsSpringWebhookApplication;

@Slf4j
@Component
public class TelegramBot implements TelegramWebhookBot {

    @Value("${telegram.bot.url}")
    private String botUrl;
    @Value("${telegram.bot.path}")
    private String botPath;

    private final TelegramClient telegramClient;
    private final UpdateProcessor updateProcessor;
    private final TelegramBotsSpringWebhookApplication botController;

    public TelegramBot(@Value("${telegram.bot.token}") String token,
                       UpdateProcessor updateProcessor,
                       TelegramBotsSpringWebhookApplication botController) {
        this.telegramClient = new OkHttpTelegramClient(token);
        this.updateProcessor = updateProcessor;
        this.botController = botController;
    }

    @PostConstruct
    public void init() {
        updateProcessor.registerBot(this);
        try {
            botController.registerBot(
                    botPath,
                    this::consumeUpdate,
                    this::runSetWebhook,
                    this::runDeleteWebhook
            );
        } catch (TelegramApiException e) {
            log.error("Error registering bot!");
        }
    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void runDeleteWebhook() {
        try {
            telegramClient.execute(new DeleteWebhook());
        } catch (TelegramApiException e) {
            log.info("Error deleting webhook!");
        }
    }

    @Override
    public void runSetWebhook() {
        try {
            telegramClient.execute(SetWebhook
                    .builder()
                    .url(botUrl + "/" + getBotPath())
                    .build());
        } catch (TelegramApiException e) {
            log.info("Error setting webhook!");
        }
    }

    @Override
    public BotApiMethod<?> consumeUpdate(Update update) {
        updateProcessor.processUpdate(update);
        return null;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }
}
