package com.example.project_sem4_springboot_api.controller;

//import com.example.project_sem4_springboot_api.config.EmailConfig;
import com.example.project_sem4_springboot_api.controller.service.WebSocketController;
import com.example.project_sem4_springboot_api.entities.request.LoginRequest;
import com.example.project_sem4_springboot_api.entities.request.Notifications;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.example.project_sem4_springboot_api.service.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;

//import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
//    private final EmailConfig emailConfig;

    private final WebSocketController webSocketController;

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
    public ResponseEntity<?> testDemo () throws IOException {
//        var subjects = List.of(1,2,3,4,5,6,7,8,9,10);
//        var teachers = List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
//        int check = 1;
//        for(int i=1;i<=teachers.size();i++){
//            System.out.println(check);
//            check = i%5==0 ? 1 : check +1;
//        }
//        emailConfig.sendmail("hnamafm17397@gmail.com");

        webSocketController.sendMessage(Notifications.builder()
                        .content("Hello")
                        .sender("teacher")
                        .type(Notifications.NTF_Type.TKB)
                .build());

        return ResponseEntity.ok("teacher"+1+"@gmail.com");
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }


}
