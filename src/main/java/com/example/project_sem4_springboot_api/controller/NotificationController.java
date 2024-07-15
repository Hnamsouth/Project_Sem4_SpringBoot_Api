package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.entities.UserDeviceToken;
import com.example.project_sem4_springboot_api.entities.request.UserDeviceTokenReq;
import com.example.project_sem4_springboot_api.exception.DataExistedException;
import com.example.project_sem4_springboot_api.repositories.UserDeviceTokenRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import com.example.project_sem4_springboot_api.service.impl.AuthService;
import com.example.project_sem4_springboot_api.service.impl.FCMService;
import com.example.project_sem4_springboot_api.service.impl.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final FCMService fcmService;
    private final UserNotificationService userNotificationService;
    private final UserRepository userRepository;
    private final UserDeviceTokenRepository userDeviceTokenRepository;

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

    @PostMapping("/create-update-user-device-token")
    public ResponseEntity<?> updateUserDeviceToken(UserDeviceTokenReq data){
        return userNotificationService.updateUserDeviceToken(data);
    }

    @GetMapping("/get-user-notifications")
    public ResponseEntity<?> getUserNotifications(@RequestParam Long page,@RequestParam Long size){
        return userNotificationService.getUserNotifications(page,size);
    }
    @PutMapping("/update-user-notifications-read-status")
    public ResponseEntity<?> updateUserNotificationsReadStatus(@RequestParam Long size){
        return userNotificationService.updateUserNotificationsReadStatus(size);
    }




}
