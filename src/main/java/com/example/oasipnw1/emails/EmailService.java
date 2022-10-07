package com.example.oasipnw1.emails;

public interface EmailService {
    public void sendSimpleMail(String to, String subject, String text);
}
