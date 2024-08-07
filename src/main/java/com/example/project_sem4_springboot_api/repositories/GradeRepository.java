package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Grade;
import com.example.project_sem4_springboot_api.entities.enums.EGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade,Long> {
    List<Grade> findAllByIdIn(List<Long> ids);
    Grade findByName(EGrade name);
}
