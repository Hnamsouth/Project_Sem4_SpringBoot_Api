package com.example.project_sem4_springboot_api.security.service;

import com.example.project_sem4_springboot_api.entities.Permission;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.repositories.PermissionRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Autowired
    private static PermissionRepository permissionRepository;
    private Long id;

    private String username;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
//        var authorities1 = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .collect(Collectors.toList());
//        // add permission
//        PermissionRepository pmsRepo = null;
//        pmsRepo.findAllByRoles(user.getRoles());
//        authorities1.add(new SimpleGrantedAuthority(user.getUsername().equals("user1") ? "admin:read":"admin:create"));
//        return authorities;

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        List<Permission> permissions = permissionRepository.findAllByRoles(user.getRoles());
        if(!permissions.isEmpty()){
            authorities.addAll(permissions.stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName().getPermission()))
                    .toList()
            );
        }

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}