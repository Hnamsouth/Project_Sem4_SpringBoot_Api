package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
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
    private Date releaseAt;
    private String note;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_id")
    private TeacherSchoolYear teacherSchoolYear;
    @ManyToOne
    @JoinColumn(name = "schoolyear_class_id")
    private SchoolYearClass schoolYearClass;
    @ManyToOne
    @JoinColumn(name = "schoolyear_subject_id")
    private SchoolYearSubject schoolYearSubject;
    // foreign key
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Attendance> attendances;


}
