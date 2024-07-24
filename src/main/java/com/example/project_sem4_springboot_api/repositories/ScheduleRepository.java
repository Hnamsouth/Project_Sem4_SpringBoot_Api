package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Schedule;
import com.example.project_sem4_springboot_api.entities.SchoolYearClass;
import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {


    boolean existsByDayOfWeekAndStudyTimeAndIndexLessonAndSchoolYearClass_IdAndCalendarRelease_Id(DayOfWeek dayOfWeek, StudyTime studyTime, int indexLesson, Long schoolYearClass_id, Long calendarRelease_id);
    boolean existsByDayOfWeekAndIndexLessonAndTeacherSchoolYear_Id(DayOfWeek dayOfWeek, int indexLesson, Long teacherSchoolYear_id);
    List<Schedule> findAllBySchoolYearClass_Grade_IdOrSchoolYearClass_SchoolYear_IdOrTeacherSchoolYear_Id(
             Long grade_id, Long schoolYear_id, Long teacherSchoolYear_id);
    List<Schedule> findAllBySchoolYearClassAndCalendarRelease_Status(SchoolYearClass schoolYearClass, boolean calendarRelease_status);
    List<Schedule> findAllBySchoolYearClassAndCalendarRelease_Id(SchoolYearClass schoolYearClass, Long calendarRelease_id);
}
