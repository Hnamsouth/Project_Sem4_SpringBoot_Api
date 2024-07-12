package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.HomeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeWorkRepository extends JpaRepository<HomeWork, Long> {

    List<HomeWork> findByTeacherSchoolYearIdAndSchoolYearSubjectIdAndSchoolYearClassId(
            Long teacherSchoolYearId, Long schoolYearSubjectId, Long schoolYearClassId);

    List<HomeWork> findByTeacherSchoolYearClassSubject_Id(Long teacherSchoolYearClassSubjectId);

    List<HomeWork> findByStudentYearHomeWorks_StudentYearInfo_Id(Long studentYearInfoId);

    List<HomeWork> findAllByTeacherSchoolYearClassSubjectId(Long teacherSchoolYearClassSubjectId);
    List<HomeWork> findAllByTeacherSchoolYearClassSubject_SchoolYearClass_Id(Long classId);
}
