package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.TeacherSchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherSchoolYearRepository extends JpaRepository<TeacherSchoolYear,Long> {
    boolean existsByTeacher_Id(Long teacherId);
    boolean existsByTeacher_IdIn(List<Long> teacherIds);

    List<TeacherSchoolYear> findAllByTeacher_IdAndSchoolYear_Id(Long teacherId, Long schoolYearId);
    List<TeacherSchoolYear> findAllByTeacher_IdOrSchoolYear_Id(Long teacherId, Long schoolYearId);


}
