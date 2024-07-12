package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.HomeWork;
import com.example.project_sem4_springboot_api.entities.StudentYearHomeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentYearHomeWorkRepository extends JpaRepository<StudentYearHomeWork, Long> {

    List<StudentYearHomeWork> findByStudentYearInfo_Id(Long studentYearInfoId);

    List<StudentYearHomeWork> findByHomeWorkId(Long homeWorkId);

    List<StudentYearHomeWork> findByStudentYearInfoId(Long studentYearInfoId);
    List<StudentYearHomeWork> findAllByStudentYearInfo_IdAndHomeWorkIn(Long studentYearInfo_id, List<HomeWork> homeWork_id);

    Optional<StudentYearHomeWork> findByStudentYearInfoIdAndHomeWorkId(Long studentYearInfoId, Long homeWorkId);


    //List<StudentYearHomeWork> findByTeacherSchoolYearClassSubjectId(Long teacherSchoolYearClassSubjectId);
}
