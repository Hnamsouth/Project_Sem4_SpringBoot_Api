package com.example.project_sem4_springboot_api.config;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.mail.*;
//import javax.mail.internet.*;
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Properties;
//
//@Service
//public class EmailConfig {
//
//    @Value("${application.security.email.host}")
//    private String host;
//    @Value("${application.security.email.port}")
//    private String post;
//    @Value("${application.security.email.username}")
//    private String username;
//    @Value("${application.security.email.password}")
//    private String password;
//    public void sendmail(String emailRecipient) throws AddressException, MessagingException, IOException {
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.port", post);
//
//        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        Message msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress(username, false));
//        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRecipient));
//        msg.setSubject("Title");
//        msg.setContent("Tutorials point email", "text/html");
//        msg.setSentDate(new Date());
//
//        MimeBodyPart messageBodyPart = new MimeBodyPart();
//        messageBodyPart.setContent("Tutorials point email", "text/html");
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//
//        MimeBodyPart attachPart = new MimeBodyPart();
//        attachPart.attachFile("D:\\Github\\Sem4\\SpringBoot\\Project_Sem4_SpringBoot_Api\\src\\main\\java\\com\\example\\project_sem4_springboot_api\\config\\test.jpeg");
//
//        multipart.addBodyPart(attachPart);
//        msg.setContent(multipart);
//
//        Transport.send(msg);
//    }
//
//}
