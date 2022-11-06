package com.example.oasipnw1.controller;

import com.example.oasipnw1.emails.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailSendTest {
    @Autowired
    private EmailService emailService;

    @CrossOrigin(origins = "*")
    @GetMapping("/test")
    public void send() {
        emailService.sendSimpleMail("wareewan.band@mail.kmutt.ac.th","test","test");
    }
}
