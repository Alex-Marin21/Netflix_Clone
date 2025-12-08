package com.netflixclone.netflix_clone.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service component responsible for handling email communications.
 * Encapsulates the logic for sending simple text-based emails using Spring's JavaMailSender.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Inject the email username from application.properties to ensure the "From" header matches
    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a simple non-HTML email to a specific recipient.
     * Constructs a SimpleMailMessage with the provided details and dispatches it via the mail sender.
     *
     * @param to      the recipient's email address
     * @param subject the subject line of the email
     * @param body    the content body of the email
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Gmail will overwrite the sender email to match the authenticated user,
        // but setting it explicitly helps with avoiding spam filters.
        message.setFrom("Netflix Clone <" + senderEmail + ">");

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}