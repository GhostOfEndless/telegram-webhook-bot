package ru.synthiq.service;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.synthiq.entity.AppDocument;
import ru.synthiq.entity.AppPhoto;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
}
