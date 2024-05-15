package ru.synthiq.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.synthiq.service.ProducerService;

import static ru.synthiq.model.RabbitQueue.ANSWER_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void produceAnswer(SendMessage message) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, message);
    }
}
