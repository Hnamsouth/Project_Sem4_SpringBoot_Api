package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findById(Long id);
    Set<Role> findByIdIn(Set<Long> id);
    Role findByName(ERole name);

    Set<Role> findAllByNameIn(List<ERole> names);

}
