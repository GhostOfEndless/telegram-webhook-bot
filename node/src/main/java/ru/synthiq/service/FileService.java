package ru.synthiq.service;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.synthiq.entity.AppDocument;
import ru.synthiq.entity.AppPhoto;
import ru.synthiq.service.enums.LinkType;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
    String generateLink(Long docId, LinkType linkType);
}
