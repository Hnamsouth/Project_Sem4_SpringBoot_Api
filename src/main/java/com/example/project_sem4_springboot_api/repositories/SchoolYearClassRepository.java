package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearClass;
import com.example.project_sem4_springboot_api.entities.TeacherSchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolYearClassRepository extends JpaRepository<SchoolYearClass, Long> {
    List<SchoolYearClass> findAllByIdOrClassNameOrTeacherSchoolYear(Long id, String className, TeacherSchoolYear teacherSchoolYear);
}
