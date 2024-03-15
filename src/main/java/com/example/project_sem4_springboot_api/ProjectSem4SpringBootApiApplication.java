package com.example.project_sem4_springboot_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ProjectSem4SpringBootApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectSem4SpringBootApiApplication.class, args);
    }



}
