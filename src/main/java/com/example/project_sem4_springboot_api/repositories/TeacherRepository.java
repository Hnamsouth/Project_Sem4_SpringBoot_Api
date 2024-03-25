package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {



}
