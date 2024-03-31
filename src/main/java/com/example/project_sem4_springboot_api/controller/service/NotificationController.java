package com.example.project_sem4_springboot_api.controller.service;

import com.example.project_sem4_springboot_api.entities.request.Notifications;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/ntf.send")
    @SendTo("/topic/tkb")
    public Notifications sendMessage(@Payload Notifications chatMessage) {
        return chatMessage;
    }

}

