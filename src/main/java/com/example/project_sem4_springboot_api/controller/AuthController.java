package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.config.EmailConfig;
import com.example.project_sem4_springboot_api.controller.service.NotificationController;
import com.example.project_sem4_springboot_api.entities.request.LoginRequest;
import com.example.project_sem4_springboot_api.entities.request.Notifications;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.example.project_sem4_springboot_api.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailConfig emailConfig;

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @GetMapping("/test-authority")
    @PreAuthorize("hasAuthority('gv:chu_nhiem') or hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> testAuth (){
        return ResponseEntity.ok("----------- success --------------");
    }
     @GetMapping("/test-api")
    public ResponseEntity<?> testApi (){
        return ResponseEntity.ok("----------- success --------------");
    }

    @GetMapping("/test-demo")
    public ResponseEntity<?> testDemo () throws MessagingException, IOException {
//        var subjects = List.of(1,2,3,4,5,6,7,8,9,10);
//        var teachers = List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
//        int check = 1;
//        for(int i=1;i<=teachers.size();i++){
//            System.out.println(check);
//            check = i%5==0 ? 1 : check +1;
//        }
//        emailConfig.sendmail("hnamafm17397@gmail.com");


        NotificationController notificationController = new NotificationController();
        notificationController.sendMessage(
                Notifications.builder()
                        .content("FIRST MESSAGE")
                        .sender("ADMIN")
                        .type(Notifications.NTF_Type.DEFAULT)
                        .build()
        );
        return ResponseEntity.ok("teacher"+1+"@gmail.com");
    }


}
