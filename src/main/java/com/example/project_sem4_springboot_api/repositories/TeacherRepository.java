package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Parent;
import com.example.project_sem4_springboot_api.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUserParent(Parent user_parent);

    List<Teacher> findAllByActive(boolean active);
}
