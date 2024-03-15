package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Permission;
import com.example.project_sem4_springboot_api.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
    List<Permission>  findAllByRoles(Set<Role> roles);
}
