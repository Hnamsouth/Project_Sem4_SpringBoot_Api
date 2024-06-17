package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScopeRepository extends JpaRepository<Scope,Long> {
    List<Scope> findAllByIdIn(List<Long> listScopeId);
}
