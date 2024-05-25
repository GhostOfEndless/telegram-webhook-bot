package ru.synthiq.service.impl;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.synthiq.dao.AppUserDAO;
import ru.synthiq.dto.MailParams;
import ru.synthiq.entity.AppUser;
import ru.synthiq.entity.enums.UserState;
import ru.synthiq.service.AppUserService;
import ru.synthiq.utils.CryptoTool;

import static ru.synthiq.entity.enums.UserState.BASIC_STATE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserDAO appUserDAO;
    private final CryptoTool cryptoTool;

    @Value("${telegram.service.mail.uri}")
    private String mailServiceUri;

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "Вы уже зарегистрированы";
        } else if (appUser.getEmail() != null) {
            return "Вам на почту уже было отправлено письмо. " +
                    "Перейдите по ссылки в письме для подтверждения регистрации.";
        }
        appUser.setState(UserState.WAIT_FOR_EMAIL_STATE);
        appUserDAO.save(appUser);
        return "Введите, пожалуйста, ваш email: ";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            return "Введите, пожалуйста, корректный email. Для отмены команды введите /cancel";
        }

        var optional = appUserDAO.findByEmail(email);
        if (optional.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(BASIC_STATE);

            appUser = appUserDAO.save(appUser);

            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            var response = sendRequestToMailService(cryptoUserId, email);

            if (response.getStatusCode() != HttpStatus.OK) {
                var msg = String.format("Отправка эл. письма на почту %s не удалась.", email);
                log.error(msg);
                appUser.setEmail(null);
                appUserDAO.save(appUser);
                return msg;
            }

            return "Вам на почту было отправлено письмо. " +
                    "Перейдте по ссылке в пиьсме для подтверждения регистрации.";
        } else {
            return "Этот email уже используется. Введите корректный email. " +
                    "Для отмены команды введите /cancel";
        }
    }

    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();

        var request = new HttpEntity<>(mailParams, headers);
        return restTemplate.exchange(mailServiceUri,
                HttpMethod.POST,
                request,
                String.class);
    }
}
