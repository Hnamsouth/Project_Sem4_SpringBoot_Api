package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.TeacherSchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TeacherSchoolYearRepository extends JpaRepository<TeacherSchoolYear,Long> {
    boolean existsByTeacher_IdAndSchoolYear_Id(Long teacher_id, Long schoolYear_id);
    boolean existsByTeacher_IdInAndSchoolYear_Id(List<Long> teacher_id, Long schoolYear_id);

    List<TeacherSchoolYear> findAllByTeacher_IdAndSchoolYear_Id(Long teacherId, Long schoolYearId);
    List<TeacherSchoolYear> findAllByTeacher_IdOrSchoolYear_Id(Long teacherId, Long schoolYearId);
    TeacherSchoolYear findByTeacherId(Long id);


}
