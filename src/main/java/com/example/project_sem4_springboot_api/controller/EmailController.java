package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.service.impl.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    public String sendSimpleEmail() {
         this.emailService.sendEmailFromTemplateSync("tulat2002@gmail.com", "anktus",
                 "send.html", "anktu", "ok");

        return "ok";
    }

}