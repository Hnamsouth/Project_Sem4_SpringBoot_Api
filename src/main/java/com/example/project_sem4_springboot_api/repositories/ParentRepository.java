package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {
}
