package com.example.springsecurity.security.services;

import org.springframework.mail.SimpleMailMessage;


public interface EmailService {
    void sendEmail(SimpleMailMessage email);
}