package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudentYearInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StudentYearInfoRepository extends JpaRepository<StudentYearInfo, Long> {


    Page<StudentYearInfo> findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_Id(
            Long schoolYearClass_id, Long schoolYearClass_schoolYear_id,Pageable pageable
    );
    List<StudentYearInfo> findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_Id(
            Long schoolYearClass_id, Long schoolYearClass_schoolYear_id
    );
    StudentYearInfo findByStudents_IdOrderByCreatedAtAsc(Long students_id);
}
