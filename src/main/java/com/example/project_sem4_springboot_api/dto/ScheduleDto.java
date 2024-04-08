package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.SchoolYearClass;
import com.example.project_sem4_springboot_api.entities.SchoolYearSubject;
import com.example.project_sem4_springboot_api.entities.TeacherSchoolYear;
import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class ScheduleDto {
    private Long id;
    private int indexLesson;
    private StudyTime studyTime;
    private DayOfWeek dayOfWeek;
    private Date releaseAt;
    private String note;
    private TeacherSchoolYear teacherSchoolYear;
    private SchoolYearClass schoolYearClass;
    private SchoolYearSubject schoolYearSubject;
}
