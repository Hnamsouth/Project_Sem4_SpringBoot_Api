package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.config.JwtService;
import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.request.LoginRequest;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.example.project_sem4_springboot_api.entities.response.AuthResponse;
import com.example.project_sem4_springboot_api.entities.response.MessageResponse;
import com.example.project_sem4_springboot_api.repositories.RoleRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public ResponseEntity<?> register (RegisterRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        Set<Long> rolesReq = request.getRole();
        Set<Role> roles = roleRepo.findByIdIn(rolesReq);
        System.out.println(roles.size());
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
                .build();
        var saveUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        // save user detail info



        var resp = new AuthResponse().builder().token(jwtToken).build();
        return ResponseEntity.ok(resp);
    }

    public ResponseEntity<?> login (LoginRequest request){
//
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        var resp = new AuthResponse().builder().token(jwtToken).build();
        return ResponseEntity.ok(resp);
    }

}