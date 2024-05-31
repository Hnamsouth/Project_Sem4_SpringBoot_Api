package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

    List<Teacher> findAllByActive(boolean active);
    List<Teacher> findAllByIdIn(List<Long> id);
}
