package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findAllByActive(boolean active);
    List<Teacher> findAllByIdIn(Collection<Long> id);
}
