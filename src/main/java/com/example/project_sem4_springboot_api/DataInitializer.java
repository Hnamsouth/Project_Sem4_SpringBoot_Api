package com.example.project_sem4_springboot_api;

import com.example.project_sem4_springboot_api.entities.Permission;
import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.enums.EPermission;
import com.example.project_sem4_springboot_api.entities.enums.ERole;
import com.example.project_sem4_springboot_api.repositories.PermissionRepository;
import com.example.project_sem4_springboot_api.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;


    @PostConstruct
    public void initializeData() {

        // Create Permission
        if(permissionRepository.findAll().isEmpty()){
            for (EPermission permission : EPermission.values()) {
                permissionRepository.save(new Permission(permission));
            }
        }
        // Create Role
        if(roleRepository.findAll().isEmpty()){
            for (ERole role : ERole.values()) {
                roleRepository.save(new Role(role));
            }
        }
        // create role - permission
        if(roleRepository.findAll().size() > 0){
            roleRepository.findAll().forEach(role ->{
                if(role.getPermission().size()==0){
                    List<Permission> lp = permissionRepository.findAll().stream()
                            .filter(p -> p.getName().toString().substring(0,3).contains(role.getName().toString().substring(5)))
                            .toList();
                    role.setPermission(lp);
                    roleRepository.save(role);
                }
            });
        }

    }
}