package com.oss.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username:no-reply@oss.com}")
    private String sender;

    @Async
    public void sendWelcomeEmail(String to, String firstName) {
        try {
            String subject = "Welcome to OSS - Registration Successful";
            String body = "<h1>Welcome!</h1><p>Hi " + firstName + ", your account has been created successfully.</p>";

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(message);
            log.info("Welcome email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email.", e);
        }
    }
}
