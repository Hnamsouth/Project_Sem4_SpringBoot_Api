package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudentYearHomeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentYearHomeWorkRepository extends JpaRepository<StudentYearHomeWork, Long> {

    List<StudentYearHomeWork> findByStudentYearInfo_Id(Long studentYearInfoId);

    List<StudentYearHomeWork> findByHomeWorkId(Long homeWorkId);

    List<StudentYearHomeWork> findByStudentYearInfoId(Long studentYearInfoId);


    //List<StudentYearHomeWork> findByTeacherSchoolYearClassSubjectId(Long teacherSchoolYearClassSubjectId);
}
