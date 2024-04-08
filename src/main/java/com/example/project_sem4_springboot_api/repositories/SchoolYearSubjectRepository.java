package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearSubject;
import com.example.project_sem4_springboot_api.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolYearSubjectRepository extends JpaRepository<SchoolYearSubject,Long> {
    SchoolYearSubject findBySubject_Name(String name);
    boolean existsBySubject_Id(Long subjectId);
    boolean existsBySubject_IdIn(List<Long> subjectIds);
    List<SchoolYearSubject> findAllBySchoolYear_IdAndSubject_IdIn(Long schoolYearId, List<Long> subjectIds);
    List<SchoolYearSubject> findAllBySchoolYear_IdOrSubject_IdIn(Long schoolYearId, List<Long> subjectIds);
}
