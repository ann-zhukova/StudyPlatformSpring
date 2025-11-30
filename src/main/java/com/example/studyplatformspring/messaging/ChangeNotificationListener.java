package com.example.studyplatformspring.messaging;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ChangeNotificationListener {

    private final JavaMailSender mailSender;
    private final String[] recipients;

    public ChangeNotificationListener(JavaMailSender mailSender,
                                      @Value("${app.notification.recipients:}") String recipients) {
        this.mailSender = mailSender;
        if (recipients == null || recipients.isBlank()) {
            this.recipients = new String[0];
        } else {
            this.recipients = recipients.split(",");
        }
    }

    @JmsListener(destination = "${app.jms.entity-change-destination}")
    public void onEntityChange(EntityChangeEvent event) {
        // отправляем сообщения когда задача завершена
        if (!"Task".equals(event.getEntityType())) {
            return;
        }
        if (!"UPDATE".equalsIgnoreCase(event.getChangeType())) {
            return;
        }
        if (event.getDetails() == null || !event.getDetails().contains("COMPLETED")) {
            return;
        }
        if (recipients.length == 0) {
            return;
        }

        sendEmail(event);
    }

    private void sendEmail(EntityChangeEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            helper.setTo(recipients);
            helper.setSubject("Task status changed to COMPLETED (ID: " + event.getEntityId() + ")");

            String body = "Entity type: " + event.getEntityType() + "\n"
                    + "Entity ID: " + event.getEntityId() + "\n"
                    + "Change type: " + event.getChangeType() + "\n"
                    + "Timestamp: " + event.getTimestamp() + "\n"
                    + "Details: " + event.getDetails();

            helper.setText(body, false);

            mailSender.send(message);
        } catch (MessagingException e) {
            
        }
    }
}

