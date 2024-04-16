package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
    private UserDetail userDetail;
}