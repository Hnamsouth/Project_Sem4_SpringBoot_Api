package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.config.JwtService;
import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.enums.TokenRequest;
import com.example.project_sem4_springboot_api.entities.request.LoginRequest;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.example.project_sem4_springboot_api.entities.response.AuthResponse;
import com.example.project_sem4_springboot_api.entities.response.LoginResponse;
import com.example.project_sem4_springboot_api.exception.AuthException;
import com.example.project_sem4_springboot_api.exception.DataExistedException;
import com.example.project_sem4_springboot_api.repositories.RoleRepository;
import com.example.project_sem4_springboot_api.repositories.UserDetailRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import com.example.project_sem4_springboot_api.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    static public final String LOGIN_TOKEN = "LOGIN_TOKEN";
    static public final String REGISTER = "REGISTER";
    static public final String REFRESH_TOKEN = "REFRESH_TOKEN";

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    public final  PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> register (RegisterRequest request)  {
        if(userRepository.existsByUsername(request.getUsername())) throw new DataExistedException("Username is already taken !!!");
        Set<Role> roles = roleRepo.findByIdIn(request.getRole());
        if(roles.isEmpty()) throw  new NullPointerException("Role not found !!!");
        var user = request.toUser(roles,passwordEncoder.encode(request.getPassword()));
        var saveUser = userRepository.save(user);
        // save user detail info
        var userDetail = request.toUserDetail(saveUser);
        userDetailRepository.save(userDetail);
        // generate token
        return returnUserInfo(user,UserDetailsImpl.build(saveUser),REFRESH_TOKEN);
    }

    public ResponseEntity<?> login (LoginRequest request){
        System.out.println(request);
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            return returnUserInfo(user,UserDetailsImpl.build(user),LOGIN_TOKEN);
        }catch (Exception e){
            throw new UsernameNotFoundException("invalid username or password");
        }
    }

    /**
     * Refresh token
     *
     * @param refreshToken
     * @param type
     * @return ResponseEntity<?>
     */
    public ResponseEntity<?> refreshToken(TokenRequest refreshToken, String type) throws  RuntimeException {
        final String username;
        if(jwtService.validateJwtToken(refreshToken.getToken())){
            username = jwtService.extractUsername(refreshToken.getToken());
            var user = userRepository.findByUsername(username).orElseThrow(
                    () -> new RuntimeException("User not found !!!")
            );
            UserDetailsImpl userDetail = UserDetailsImpl.build(user);
            return returnUserInfo(user,userDetail,type);
        }
        throw new AuthException("Token is invalid !!!");
    }

    private ResponseEntity<?> returnUserInfo(User user,UserDetailsImpl userDetail,String type){
        var jwtToken = jwtService.generateToken(userDetail);
        var refreshToken = jwtService.generateRefreshToken(userDetail);
        var resp = AuthResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
        if(type.equals(LOGIN_TOKEN) || type.equals(REGISTER)){
            return ResponseEntity.ok(
                LoginResponse.builder().id(user.getId()).username(user.getUsername()).authResponse(resp)
                    .roles(user.getRoles()).userDetail(user.getUserDetail().get(0).getDto(false))
                    .permissions(user.getRoles().stream().toList().get(0).getPermission()).build());
        }else{
            return ResponseEntity.ok().body(resp);
        }
    }

    public ResponseEntity<?> testDemo(RegisterRequest data){
        try {
//            var user = userMapper.toEntity(data);
            return ResponseEntity.ok("asd");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}