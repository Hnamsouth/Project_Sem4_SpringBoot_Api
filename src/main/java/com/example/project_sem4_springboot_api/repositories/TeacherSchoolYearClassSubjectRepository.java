package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.TeacherSchoolYearClassSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherSchoolYearClassSubjectRepository extends JpaRepository<TeacherSchoolYearClassSubject, Long> {
    List<TeacherSchoolYearClassSubject> findAllBySchoolYearClass_Id(Long schoolYearClass_id);
    boolean findByTeacherSchoolYear_IdAndSchoolYearClass_IdAndTeacherSchoolYear_Id(Long teacherSchoolYear_id, Long schoolYearClass_id, Long teacherSchoolYear_id2);
}
