package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher_schoolyear_class_subject")
public class TeacherSchoolYearClassSubject {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schoolyear_class_id")
    private SchoolYearClass schoolYearClass;
    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_id")
    private TeacherSchoolYear teacherSchoolYear;

    @ManyToOne
    @JoinColumn(name = "schoolyear_subject_id")
    private SchoolYearSubject schoolYearSubject;

    // foreign key

}
