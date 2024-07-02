package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudyResult;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StudyResultRepository extends JpaRepository<StudyResult,Long> {

    List<StudyResult> findAllBySemesterAndStudentYearInfo_SchoolYearClass_Id(ESem semester, Long schoolYearClass_id);
    List<StudyResult> findAllBySemesterAndStudentYearInfo_Id(ESem semester, Long studentYearInfo_id);

    List<StudyResult> findAllByStudentYearInfo_IdInAndSemester(List<Long> studentYearInfo_id, ESem semester);


}