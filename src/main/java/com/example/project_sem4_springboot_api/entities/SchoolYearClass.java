package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private Grade grade;
    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonManagedReference
    private Room room;
    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_id")
    @JsonManagedReference
    private TeacherSchoolYear teacherSchoolYear;
    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    @JsonManagedReference
    private SchoolYear schoolYear;

    // foreign key
    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudentYearInfo> studentYearInfos;

    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<TeacherSchoolYearClassSubject> teacherSchoolYearClassSubjects;

    public SchoolYearClass toRes() {
        return SchoolYearClass.builder().id(id).classCode(classCode).className(className).grade(grade).build();
    }

    // equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchoolYearClass )) return false;
        return id != null && id.equals(((SchoolYearClass) o).getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
