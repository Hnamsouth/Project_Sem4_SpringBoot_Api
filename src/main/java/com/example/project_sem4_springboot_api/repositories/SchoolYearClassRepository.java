package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearClass;
import com.example.project_sem4_springboot_api.entities.TeacherSchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface SchoolYearClassRepository extends JpaRepository<SchoolYearClass, Long> {

    List<SchoolYearClass> findAllByIdIn(List<Long> ids);
    SchoolYearClass findByTeacherSchoolYear_Id(Long ids);

    List<SchoolYearClass> findAllByIdOrClassNameOrClassCodeOrTeacherSchoolYear_IdOrSchoolYear_IdOrGrade_IdOrRoom_Id(
            Long id, String className, String classCode,
            Long teacherSchoolYear_id, Long schoolYear_id, Long grade_id, Long room_id
    );
    List<SchoolYearClass> findAllBySchoolYear_IdAndGrade_Id(Long schoolYear_id, Long grade_id);
    List<SchoolYearClass> findAllByTeacherSchoolYear_IdAndSchoolYear_Id(Long teacherSchoolYear_id, Long schoolYear_id);
    List<SchoolYearClass> findAllBySchoolYear_Id(Long schoolYear_id);
    List<SchoolYearClass> findAllByGrade_IdIn(List<Long> grade_id);

    List<SchoolYearClass> findAllByTeacherSchoolYear_Id(Long id);

    boolean existsByClassNameAndSchoolYear_Id(String className, Long schoolYear_id);
    boolean existsByTeacherSchoolYear_IdAndSchoolYear_Id(Long teacherSchool_id, Long schoolYear_id);
}
