package com.example.project_sem4_springboot_api.controller.service;

import com.example.project_sem4_springboot_api.entities.request.Notifications;
import com.example.project_sem4_springboot_api.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {


    @GetMapping("/test-authority")
    @PreAuthorize("hasAnyAuthority('update:student','update:user') or hasAnyRole('ROLE_BGH') or hasAuthority('read:user')")
    public ResponseEntity<?> testAuth (){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/get-auth")
    public ResponseEntity<?> testAuth2 (){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }
    @GetMapping("/test-api")
    public ResponseEntity<?> testApi (){
        return ResponseEntity.ok("-----------  success  --------------");
    }

}

