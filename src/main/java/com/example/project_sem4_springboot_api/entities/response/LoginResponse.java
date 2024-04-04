package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.Permission;
import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Builder
@Data
public class LoginResponse {
    private Long id;
    private String username;
    private AuthResponse  authResponse;
    private Set<Role> roles;
    private List<Permission> permissions;
    private UserDetail userDetail;
}
