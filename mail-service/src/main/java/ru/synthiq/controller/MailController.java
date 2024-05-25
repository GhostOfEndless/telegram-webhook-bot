package ru.synthiq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.synthiq.dto.MailParams;
import ru.synthiq.service.EmailSenderService;

@RequestMapping("/mail")
@RestController
@RequiredArgsConstructor
public class MailController {

    private final EmailSenderService emailSenderService;

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams) {
        emailSenderService.send(mailParams);
        return ResponseEntity.ok().build();
    }
}
