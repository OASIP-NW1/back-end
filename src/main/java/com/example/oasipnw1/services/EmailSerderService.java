//package com.example.oasipnw1.services;
//
//import com.example.oasipnw1.emails.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailSerderService implements EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    //    @Value("${spring.mail.username}")
////    private String from;
////
////    @Autowired
////    public EmailSerderService(JavaMailSender javaMailSender){
////        this.javaMailSender = javaMailSender;
////    }
////
////    public void sendNotification(String user, String subject, String text) throws MailException {
////        SimpleMailMessage mailMessage = new SimpleMailMessage();
////        mailMessage.setTo(user);
////        mailMessage.setFrom(from);
////        mailMessage.setSubject(subject);
////        mailMessage.setText(text);
////
////        javaMailSender.send(mailMessage);
////        System.out.printf("Email send Successful");
////    }
//    @Override
//    public void sendSimpleMail(String to, String subject, String text) {
//        // Creating a simple mail message
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        // Setting up necessary details
//        mailMessage.setFrom("noreply@oasip.nw1");
//        mailMessage.setTo(to);
//        mailMessage.setText(text);
//        mailMessage.setSubject(subject);
//        // Sending the mail
//        javaMailSender.send(mailMessage);
//    }
//}