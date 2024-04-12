package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Permission;
import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.enums.EPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
    List<Permission>  findAllByRoles(List<Role> roles);
    Permission findByName(EPermission name);
    List<Permission> findAllByNameIn(List<EPermission> names);

//    @Query("select p from Permission p where (:prefix is NULL OR p.name LIKE %:prefix% ) " )
//    List<Permission>  filterPermission(@Param("prefix") String prefix);
}
