package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.entities.enums.TokenRequest;
import com.example.project_sem4_springboot_api.entities.request.LoginRequest;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.example.project_sem4_springboot_api.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/login-token")
    public ResponseEntity<?> login (@Valid @RequestBody TokenRequest token) throws IOException {
        return authService.refreshToken(token, AuthService.LOGIN_TOKEN);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRequest refreshToken) throws IOException {
        return authService.refreshToken(refreshToken, AuthService.REFRESH_TOKEN);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @GetMapping("/test-authority")
    @PreAuthorize("hasAuthority('gv:chu_nhiem') or hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> testAuth (){
        return ResponseEntity.ok("----------- success --------------");
    }

    @GetMapping("/test-auth")
    // required login token valid
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GV','ROLE_PH','ROLE_BGH','ROLE_DEV','ROLE_NV_TC','ROLE_NV_TV','ROLE_NV_VT')")
    public ResponseEntity<?> testAuth2 (){
        return ResponseEntity.ok("----------- success --------------");
    }
     @GetMapping("/test-api")
    public ResponseEntity<?> testApi (){
        return ResponseEntity.ok("-----------  success  --------------");
    }

    @GetMapping("/test-demo")
    public ResponseEntity<?> testDemo (RegisterRequest data) throws IOException {
        return authService.testDemo(data);
    }




}
