package com.homework.notificationservice.kafka;

import com.homework.notificationservice.dto.UserEvent;
import com.homework.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consume(UserEvent userEvent) {
        emailService.sendEmailFromEvent(userEvent);
    }
}
