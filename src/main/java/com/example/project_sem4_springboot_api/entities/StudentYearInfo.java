package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_year_info")
public class StudentYearInfo {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sem;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student students;
    @ManyToOne
    @JoinColumn(name = "schoolyear_class_id")
    private SchoolYearClass schoolYearClass;
    // foreign
    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<StudentFee> studentFee;
    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TestPoint> testPoints;

}