package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentStatusRepository extends JpaRepository<StudentStatus,Long> {
}
