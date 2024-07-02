package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearSubjectGrade;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchoolYearSubjectGradeRepository extends JpaRepository<SchoolYearSubjectGrade,Long> {
    List<SchoolYearSubjectGrade> findAllByGrade_IdAndNumber(Long gradeId, int number);

    List<SchoolYearSubjectGrade> findAllByGrade_IdAndSemIsLike(Long grade_id, ESem sem);
    List<SchoolYearSubjectGrade> findAllByGrade_Id(Long gradeId);

    List<SchoolYearSubjectGrade> findAllByIdOrSchoolYearSubject_Id(Long id, Long schoolYearSubjectId);
    List<SchoolYearSubjectGrade> findAllBySchoolYearSubject_IdAndGrade_Id(Long schoolYearSubject_id, Long grade_id);
    //get all Period of year and grade
    @Query("SELECT sum(s.number) FROM SchoolYearSubjectGrade s WHERE s.grade.id = :gradeId AND s.schoolYearSubject.schoolYear.id = :schoolYearId")
    int getPeriodOfSchoolYearAndGrade(@Param("gradeId") Long gradeId,@Param("schoolYearId") Long schoolYearId);



}
