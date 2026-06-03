package com.homework.notificationservice.controller;

import com.homework.notificationservice.dto.EmailRequest;
import com.homework.notificationservice.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailRequest request) {
        emailService.sendEmail(request);
        return ResponseEntity.ok("Письмо успешно отправлено на: " + request.getEmail());
    }
}

