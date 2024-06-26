package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import com.example.project_sem4_springboot_api.entities.response.ScheduleResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedule {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int indexLesson;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyTime studyTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private String note;
    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_id")
    @JsonManagedReference
    @JsonIgnoreProperties({"schoolYear"})
    private TeacherSchoolYear teacherSchoolYear;
    @ManyToOne
    @JoinColumn(name = "schoolyear_class_id")
    @JsonManagedReference
    private SchoolYearClass schoolYearClass;
    @ManyToOne
    @JoinColumn(name = "schoolyear_subject_id")
    @JsonManagedReference
    private SchoolYearSubject schoolYearSubject;

    @ManyToOne
    @JoinColumn(name = "calendar_release_id")
    @JsonManagedReference
    private CalendarRelease calendarRelease;
    // foreign key

    @JsonIgnore
    public ScheduleResponse toScheduleResponse(){
        return ScheduleResponse.builder()
                .id(id).indexLesson(indexLesson).studyTime(studyTime).dayOfWeek(dayOfWeek)
                .note(note).teacherSchoolYearId(teacherSchoolYear.getId()).teacherSchoolYearName(teacherSchoolYear.getTeacher().getSortName())
                .schoolYearClassId(schoolYearClass.getId()).SchoolYearClassName(schoolYearClass.getClassName())
                .schoolYearSubjectId(schoolYearSubject.getId()).SchoolYearSubjectName(schoolYearSubject.getSubject().getName())
                .build();
    }

}
