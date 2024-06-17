package com.example.project_sem4_springboot_api.service;

import org.springframework.mail.MailSender;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    String sendMail(MultipartFile[] file, String to, String[] cc, String subject, String body);

}
