package com.example.project_sem4_springboot_api.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

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
    @JsonManagedReference
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    @JsonManagedReference
    private SchoolYear schoolYear;

    // foreign key
    @OneToMany(mappedBy = "teacherSchoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "teacherSchoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<SchoolYearClass> schoolYearClass;

    @OneToMany(mappedBy = "teacherSchoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudentScoreSubject> studentScoreSubjects;

    @OneToMany(mappedBy = "teacherSchoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<TeacherSchoolYearClassSubject> teacherSchoolYearClassSubjects;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_department",
            joinColumns = @JoinColumn(name="teacher_schoolyear_id"),
            inverseJoinColumns = @JoinColumn(name="department_id")
    )
    @JsonBackReference
    private List<Department> departments;

    // equals and hashcode

    @JsonIgnore
    public TeacherSchoolYear toRes(){
        return TeacherSchoolYear.builder()
                .id(this.id)
                .teacher(teacher.toResWithoutUser())
                .schoolYear(this.schoolYear)
                .build();
    }

    @JsonIgnore
    public Map<String,Object> toTeacherInfo(){
        return teacher.toTeacherInfo();
    }

}
