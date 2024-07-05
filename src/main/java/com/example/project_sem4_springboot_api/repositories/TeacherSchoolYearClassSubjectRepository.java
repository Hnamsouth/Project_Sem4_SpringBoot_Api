package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearClass;
import com.example.project_sem4_springboot_api.entities.TeacherSchoolYearClassSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherSchoolYearClassSubjectRepository extends JpaRepository<TeacherSchoolYearClassSubject, Long> {
    List<TeacherSchoolYearClassSubject> findAllBySchoolYearClass_Id(Long schoolYearClass_id);
    List<TeacherSchoolYearClassSubject> findAllByIdIn(List<Long> ids);
    Optional<TeacherSchoolYearClassSubject> findBySchoolYearClass_IdAndSchoolYearSubject_Id(Long schoolYearClass_id, Long schoolYearSubject_id);
    List<TeacherSchoolYearClassSubject> findAllByTeacherSchoolYear_SchoolYear_Id(Long schoolYear_id);
    List<TeacherSchoolYearClassSubject> findAllByTeacherSchoolYear_Id(Long teacherSchoolYear_id);
    //int findTotalStudentsByTeacherSchoolYearClassSubjectId(Long teacherSchoolYearClassSubjectId);



    @Query("SELECT tscs.schoolYearClass FROM TeacherSchoolYearClassSubject tscs WHERE tscs.teacherSchoolYear.id = :teacherSchoolYear_id GROUP BY tscs.schoolYearClass.id")
    List<SchoolYearClass> getClassListIdByTeacherSchoolYear_Id(@Param("teacherSchoolYear_id") Long teacherSchoolYear_id);

}
