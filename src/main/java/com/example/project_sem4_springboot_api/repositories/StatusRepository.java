package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status,Long> {
    Status findByCode(String code);
}
