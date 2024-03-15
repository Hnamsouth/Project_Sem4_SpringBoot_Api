package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.entities.request.LoginRequest;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.example.project_sem4_springboot_api.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
//    @PreAuthorize("")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/test-authority")
    @PreAuthorize("hasAuthority('gv:chu_nhiem')")
    public ResponseEntity<?> testAuth (){
        return ResponseEntity.ok("----------- success --------------");
    }

}
