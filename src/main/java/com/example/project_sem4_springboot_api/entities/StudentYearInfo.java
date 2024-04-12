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
@Table(name = "student_year_info")
public class StudentYearInfo {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sem;
    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonManagedReference
    private Student students;
    @ManyToOne
    @JoinColumn(name = "schoolyear_class_id")
    @JsonManagedReference
    private SchoolYearClass schoolYearClass;
    // foreign
    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudentFee> studentFee;
    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<TestPoint> testPoints;

}