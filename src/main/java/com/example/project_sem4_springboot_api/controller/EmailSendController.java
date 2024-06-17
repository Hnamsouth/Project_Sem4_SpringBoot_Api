package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.service.EmailService;
import com.example.project_sem4_springboot_api.service.impl.EmailServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class EmailSendController  {

    private final EmailServiceImpl emailService;

    public EmailSendController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }
    //private final EmailService emailService;

//    public EmailSendController(EmailService emailService) {
//        this.emailService = emailService;
//    }
//
//    @PostMapping("/send")
//    public String sendEmail(@RequestParam(value = "file", required = false) MultipartFile[] file, String to, String[] cc, String subject, String body){
//        return emailService.sendMail(file, to, cc, subject, body);
//    }

    @GetMapping("/email")
    public String sendSImpleMail() {

        //this.emailService.sendSimpleMail();
         this.emailService.sendEmailSync("tulat2002@gmail.com", "test send email",
         "<h1 style=\"color: red;\"> <b> hello </b> </h1>", false,
         true);
        return "OK";
    }

}
