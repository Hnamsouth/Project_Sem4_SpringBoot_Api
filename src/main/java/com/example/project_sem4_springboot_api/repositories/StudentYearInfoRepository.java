package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudentYearInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StudentYearInfoRepository extends JpaRepository<StudentYearInfo, Long> {

    List<StudentYearInfo> findAllBySchoolYearClass_SchoolYear_Id(Long schoolYear_id);
    List<StudentYearInfo> findAllBySchoolYearClass_Id(Long schoolYear_id);
    List<StudentYearInfo> findAllBySchoolYearClass_TeacherSchoolYear_Id(Long schoolYear_id);
    List<StudentYearInfo> findAllByIdIn(List<Long> ids);
    List<StudentYearInfo> findAllBySchoolYearClass_IdIn(List<Long> schoolYear_id);
    List<StudentYearInfo> findAllBySchoolYearClass_Grade_IdInAndSchoolYearClass_SchoolYear_Id(List<Long> grade_id,Long schoolYear_id);

    Page<StudentYearInfo> findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_Id(
            Long schoolYearClass_id, Long schoolYearClass_schoolYear_id,Pageable pageable
    );
    List<StudentYearInfo> findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_Id(
            Long schoolYearClass_id, Long schoolYearClass_schoolYear_id
    );
    StudentYearInfo findByStudents_IdOrderByCreatedAtAsc(Long students_id);
}
