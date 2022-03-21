package com.jobs.sitran.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    public JavaMailSender emailSender;

    public void sendMail(String email, String subject, String content) throws MessagingException {

        MimeMessage message = this.emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        message.setContent(content, "text/html; charset=UTF-8");
        helper.setTo(email);
        helper.setSubject(subject);
        this.emailSender.send(message);
    }
}
