package com.example.project_sem4_springboot_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/demo1",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/test/api",
            "/**"
    };

    private  final JwtAuthenticationFilter jwtAuthenticationFilter;
    private  final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean

    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //
//               .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(req->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
//                      .requestMatchers(GET,"/api/v1/product/**").permitAll()
                        .requestMatchers(GET,"/api/v1/user/test-authority").hasAuthority("admin:read")
//                      .requestMatchers(DELETE,"/api/v1/product/**").hasAnyRole(String.valueOf(ROLE_MANAGER))
                        .anyRequest().authenticated()
                        )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Ko lưu phiên người dùng
                .authenticationProvider(authenticationProvider)
                // authenticate
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout( logout ->
                                logout
                                        .logoutUrl("/api/v1/auth/logout")
                                        .addLogoutHandler(logoutHandler)
                                        .logoutSuccessHandler(((request, response, authentication) -> clearContext() ))
//                                 .logoutSuccessUrl("/home")
                )
        ;

        return http.build();
    }

    // custom logout here success
    public void clearContext(){
        SecurityContextHolder.clearContext();
    }


}
