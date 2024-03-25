package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "student_year_info")
public class StudentYearInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int grade;

    private int sem;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student students;

    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    private List<TestPoint> testPoints;

    @ManyToOne
    @JoinColumn(name = "school_year_class_id")
    private SchoolYearClass schoolYearClass;

    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    private List<StudentFee> studentFee;

}