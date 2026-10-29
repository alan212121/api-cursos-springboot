package com.example.demo.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final ObjectProvider<JavaMailSender> mailSender;

    public EmailService(ObjectProvider<JavaMailSender> mailSender) {
        this.mailSender = mailSender;
    }

    public boolean enviar(String para, String asunto, String texto) {
        try {
            JavaMailSender sender = mailSender.getIfAvailable();
            if (sender == null) return false;
            SimpleMailMessage m = new SimpleMailMessage();
            m.setTo(para);
            m.setSubject(asunto);
            m.setText(texto);
            sender.send(m);
            return true;
        } catch (Exception e) {
            System.out.println("No se pudo enviar el correo: " + e.getMessage());
            return false;
        }
    }
}