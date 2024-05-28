package ru.synthiq.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.synthiq.service.UpdateProducer;
import ru.synthiq.utils.MessageUtils;

import static ru.synthiq.model.RabbitQueue.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateProcessor {

    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;
    private TelegramBot telegramBot;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessageByType(update);
        } else {
            log.error("Unsupported message type is received: {}", update);
        }
    }

    private void distributeMessageByType(Update update) {
        var message = update.getMessage();

        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileReceivedView(update);
    }

    private void setFileReceivedView(Update update) {
        var message = messageUtils.generateSendMessageWithText(update,
                "Файл получен! Обрабатывается ... ");

        setView(message);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileReceivedView(update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var message = messageUtils.generateSendMessageWithText(update,
                "Неподдерживаемый тип сообщения");

        setView(message);
    }

    public void setView(SendMessage message) {
        telegramBot.sendAnswerMessage(message);
    }
}
