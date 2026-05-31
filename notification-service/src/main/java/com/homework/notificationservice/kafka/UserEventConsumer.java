package com.homework.notificationservice.kafka;

import com.homework.notificationservice.dto.UserEvent;
import com.homework.notificationservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserEventConsumer.class);
    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consume(UserEvent userEvent) {
        try {
            emailService.sendEmailFromEvent(userEvent);
        } catch (Exception e) {
            log.error("Ошибка при отправке письма. Событие: {}, Email: {}. Причина: {}",
                    userEvent.getEventType(), userEvent.getEmail(), e.getMessage());
        }
    }
}
