package ru.synthiq.service;

import ru.synthiq.dto.MailParams;

public interface EmailSenderService {

    void send(MailParams mailParams);
}
