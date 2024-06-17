package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudentScoreSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentScoreSubjectRepository extends JpaRepository<StudentScoreSubject,Long> {
    StudentScoreSubject findBySchoolYearSubjectIdAndStudentYearInfo_Id(Long schoolYearSubjectId, Long studentYearInfoId);
    StudentScoreSubject findByStudentYearInfo_IdAndSchoolYearSubject_Id(Long studentYearInfoId, Long schoolYearSubjectId);
    List<StudentScoreSubject> findAllByStudentYearInfo_IdInAndSchoolYearSubject_Id(List<Long> studentYearInfoIds, Long schoolYearSubjectId);

    List<StudentScoreSubject> findAllByStudentYearInfo_Id(Long studentYearInfoId);
}
