package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearSubjectGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolYearSubjectGradeRepository extends JpaRepository<SchoolYearSubjectGrade,Long> {
}
