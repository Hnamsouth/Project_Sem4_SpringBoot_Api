package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Grade;
import com.example.project_sem4_springboot_api.entities.enums.EGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade,Long> {
    Grade findByName(EGrade name);
}
