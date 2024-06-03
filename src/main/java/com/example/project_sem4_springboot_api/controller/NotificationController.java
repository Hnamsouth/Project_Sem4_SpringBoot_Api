package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.service.impl.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private FCMService fcmService;

    /**
     * This method sends a notification to the user with the device token.
     * @param token The device token of the user.
     * @param title The title of the notification.
     * @param body The body of the notification.
     * @return A string indicating that the notification has been sent.
     * @throws Exception If there is an error sending the notification.
     */
    @GetMapping("/send-notification")
    public String sendNotification(@RequestParam String token, @RequestParam String title, @RequestParam String body) throws Exception {
        // token is the device token of the user
        fcmService.sendNotification(token, title, body);
        return "UserNotification sent";
    }



}
