package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String formEmail;


    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(formEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            if(files !=null){
                for(MultipartFile file :files ){
                    mimeMessageHelper.addAttachment(
                            Objects.requireNonNull(file.getOriginalFilename()),
                            new ByteArrayResource(file.getBytes())
                    );
                }
            }

            javaMailSender.send(mimeMessage);
            return "mail send";

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
