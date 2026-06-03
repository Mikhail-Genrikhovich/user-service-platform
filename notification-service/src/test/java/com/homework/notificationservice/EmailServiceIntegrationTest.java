package com.homework.notificationservice;

import com.homework.notificationservice.dto.EmailRequest;
import com.homework.notificationservice.dto.UserEvent;
import com.homework.notificationservice.service.EmailService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.mail.host=localhost",
        "spring.mail.port=3025",
        "spring.mail.username=test@test.com",
        "spring.mail.password=test",
        "spring.mail.properties.mail.smtp.auth=false",
        "spring.mail.properties.mail.smtp.starttls.enable=false"
})
public class EmailServiceIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig()
                    .withUser("test@test.com", "test"));

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("Отправка письма напрямую через API. Должно прийти одно письмо на указанный адрес.")
    void sendEmail_shouldDeliverMessageToRecipient() throws Exception {
        EmailRequest request = new EmailRequest("user@example.com", "Тестовое сообщение");

        emailService.sendEmail(request);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("user@example.com", messages[0].getAllRecipients()[0].toString());
    }

    @Test
    @DisplayName("Отправка письма при создании пользователя. Должно прийти письмо о создании аккаунта.")
    void sendEmailFromEvent_whenUserCreated_shouldDeliverMessage() throws Exception {
        UserEvent event = new UserEvent("CREATED", "newuser@example.com");

        emailService.sendEmailFromEvent(event);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("newuser@example.com", messages[0].getAllRecipients()[0].toString());

        String content = messages[0].getContent().toString();
        assertTrue(content.contains("на сайте был успешно создан"));
    }

    @Test
    @DisplayName("Отправка письма при удалении пользователя. Должно прийти письмо об удалении аккаунта.")
    void sendEmailFromEvent_whenUserDeleted_shouldDeliverMessage() throws Exception {
        UserEvent event = new UserEvent("DELETED", "deleted@example.com");

        emailService.sendEmailFromEvent(event);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("deleted@example.com", messages[0].getAllRecipients()[0].toString());

        String content = messages[0].getContent().toString();
        assertTrue(content.contains("был удалён"));
    }
}
