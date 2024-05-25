package ru.synthiq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.synthiq.service.UserActivationService;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class ActivationController {

    private final UserActivationService userActivationService;

    @GetMapping("/activation")
    public ResponseEntity<?> activation(@RequestParam String id) {
        var res = userActivationService.activation(id);

        if (res) {
            return ResponseEntity.ok().body("Регистрация успешно завершена!");
        }

        return ResponseEntity.internalServerError().build();
    }
}
