package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudentScores;
import com.example.project_sem4_springboot_api.entities.enums.EPointType;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StudentScoresRepository extends JpaRepository<StudentScores,Long> {

    List<StudentScores> findAllBySemesterNameAndStudentScoreSubject_StudentYearInfo_IdIn(ESem sem, List<Long> studentYearInfoId);
    List<StudentScores> findAllBySemesterNameAndStudentScoreSubject_StudentYearInfo_IdInAndStudentScoreSubject_SchoolYearSubject_IdAndPointType_PointTypeIn(ESem sem,List<Long> studentYearInfo_id, Long schoolYearSubject_ids, List<EPointType> pointTypes);
}
