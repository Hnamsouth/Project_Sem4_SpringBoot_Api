package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.config.JwtService;
import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import com.example.project_sem4_springboot_api.entities.request.LoginRequest;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.example.project_sem4_springboot_api.entities.response.AuthResponse;
import com.example.project_sem4_springboot_api.entities.response.LoginResponse;
import com.example.project_sem4_springboot_api.entities.response.MessageResponse;
import com.example.project_sem4_springboot_api.repositories.RoleRepository;
import com.example.project_sem4_springboot_api.repositories.UserDetailRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public ResponseEntity<?> register (RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        Set<Long> rolesReq = request.getRole();
        Set<Role> roles = roleRepo.findByIdIn(rolesReq);
//        rolesReq.forEach(r ->{
//            Optional<Role> findRole = roleRepo.findById(r.longValue());
////            ifPresent: nếu có 1 giá trị thì thực hiện còn không thì ko làm j cả
//            findRole.ifPresent(roles::add);
//        });
        if(roles.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Role Not found!"));
        }
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .status(1)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();
        var saveUser = userRepository.save(user);
        // save user detail info
        var userDetail = UserDetail.builder()
                .address(request.getAddress())
                .phone(request.getPhone())
                .birthday(request.getBirthday())
                .avatar(request.getAvatar())
                .gender(request.isGender())
                .firstname(request.getFirst_name())
                .lastname(request.getLast_name())
                .email(request.getEmail())
                .nation(request.getNation())
                .citizen_id(request.getCitizen_id())
                .user(saveUser)
                .build();
        userDetailRepository.save(userDetail);
        // generate token
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var resp = AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(resp);
    }

    public ResponseEntity<?> login (LoginRequest request){
        System.out.println(request);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            var resp = AuthResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.ok(
                    LoginResponse.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .authResponse(resp)
                            .roles(user.getRoles())
                            .permissions(user.getRoles().stream().toList().get(0).getPermission())
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws  IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = userRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                // save user token
                var refeshToken = jwtService.generateRefreshToken(user);
                var resp = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refeshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), resp);
            }
        }
    }

}