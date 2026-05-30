package com.homework.notificationservice.service;

import com.homework.notificationservice.dto.EmailRequest;
import com.homework.notificationservice.dto.UserEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String froEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(EmailRequest request) {
        sendMail(request.getEmail(), "Уведомление", request.getMessage());
    }

    public void sendEmailFromEvent(UserEvent event) {
        String message;

        if ("CREATED".equals(event.getEventType())) {
            message = "Здравствуйте! Ваш аккаунт был успешно создан.";
        } else if ("DELETED".equals(event.getEventType())) {
            message = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            return;
        }

        sendMail(event.getEmail(), "Уведомление", message);
    }

    private void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(froEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
