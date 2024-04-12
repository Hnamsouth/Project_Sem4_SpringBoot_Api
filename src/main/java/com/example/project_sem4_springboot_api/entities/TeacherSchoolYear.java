package com.example.project_sem4_springboot_api.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher_schoolyear")
public class TeacherSchoolYear {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    private SchoolYear schoolYear;

    // foreign key
    @OneToMany(mappedBy = "teacherSchoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "teacherSchoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<SchoolYearClass> schoolYearClass;

    @OneToMany(mappedBy = "teacherSchoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TeacherSchoolYearClassSubject> teacherSchoolYearClassSubjects;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_department",
            joinColumns = @JoinColumn(name="teacher_schoolyear_id"),
            inverseJoinColumns = @JoinColumn(name="department_id")
    )
    private List<Department> departments;

}
