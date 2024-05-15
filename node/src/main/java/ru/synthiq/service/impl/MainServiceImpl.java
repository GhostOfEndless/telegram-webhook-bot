package ru.synthiq.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.synthiq.dao.AppUserDAO;
import ru.synthiq.dao.RawDataDAO;
import ru.synthiq.entity.AppUser;
import ru.synthiq.entity.RawData;
import ru.synthiq.service.MainService;
import ru.synthiq.service.ProducerService;

import static ru.synthiq.entity.enums.UserState.BASIC_STATE;
import static ru.synthiq.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static ru.synthiq.service.ServiceCommands.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";

        if (CANCEL.equals(text)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            // TODO добавить обработку емейла
        } else {
            log.error("Unknown user state: {}", userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }

        var chatId = update.getMessage().getChatId().toString();
        sendAnswer(output, chatId);


    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId().toString();

        if (isNotAllowedToSendContent(chatId, appUser)) {
            return;
        }
        // TODO: добавить сохранение документа

        var answer = "Документ успешно загружен! Ссылка для скачивания  http://test.ru/get-doc/777";
        sendAnswer(answer, chatId);
    }

    private boolean isNotAllowedToSendContent(String chatId, AppUser appUser) {
        var userState = appUser.getState();
        if (!appUser.getIsActive()) {
            var error = "Зарегистрируйтесь или активируйте свою учётную запись для загрузки контента.";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "Отмените текущую команду с помошью /cancel для отправки файлов.";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId().toString();

        if (isNotAllowedToSendContent(chatId, appUser)) {
            return;
        }
        // TODO: добавить сохранение фото

        var answer = "Фото успешно загружено! Ссылка для скачивания  http://test.ru/get-photo/777";
        sendAnswer(answer, chatId);
    }

    private void sendAnswer(String output, String chatId) {
        var sendMessage = new SendMessage(chatId, output);
        producerService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String command) {
        if (REGISTRATION.equals(command)) {
            // TODO добавить регистрацию
            return "Временно недоступно";
        } else if (HELP.equals(command)) {
            return help();
        } else if (START.equals(command)) {
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
        }
    }

    private String help() {
        return """
                Список доступных команд:
                /cancel - отмена выполнения текущей команды;
                /registration - регистрация пользователя.""";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена";
    }

    private AppUser findOrSaveAppUser(Update update) {
        var telegramUser = update.getMessage().getFrom();

        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());

        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    // TODO изменить значение по умолчанию после регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();

            return appUserDAO.save(transientAppUser);
        }

        return persistentAppUser;
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        rawDataDAO.save(rawData);
    }
}
