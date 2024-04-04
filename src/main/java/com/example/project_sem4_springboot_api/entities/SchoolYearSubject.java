package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schoolyear_subject")
public class SchoolYearSubject {

    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    private SchoolYear schoolYear;
    // foreign key
    @OneToMany(mappedBy = "schoolYearSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TeacherSchoolYearClassSubject> teacherSchoolYearClassSubjects;
    @OneToMany(mappedBy = "schoolYearSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Schedule> schedules;
    @OneToMany(mappedBy = "schoolYearSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<SchoolYearSubjectGrade> schoolYearSubjectGrades;
    @OneToMany(mappedBy = "schoolYearSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TestPoint> testPoints;
}
