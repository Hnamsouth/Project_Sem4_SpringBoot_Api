package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schoolyear_class")
public class SchoolYearClass {
    // attribute
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String className;
    @Column(nullable = false)
    private String classCode;
    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_id")
    private TeacherSchoolYear teacherSchoolYear;
    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    private SchoolYear schoolYear;

    // foreign key
    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<StudentYearInfo> studentYearInfos;

    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TeacherSchoolYearClassSubject> teacherSchoolYearClassSubjects;

}
