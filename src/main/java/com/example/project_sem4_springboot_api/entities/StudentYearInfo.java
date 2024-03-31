package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_year_info")
public class StudentYearInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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