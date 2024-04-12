package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "schoolyear_subject_grade")
public class SchoolYearSubjectGrade {
    // ATB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ESem sem;
    @ManyToOne
    @JoinColumn(name = "grade_id")
    @JsonManagedReference
    private Grade grade;
    @ManyToOne
    @JoinColumn(name = "schoolyear_subject_id")
    @JsonManagedReference
    private SchoolYearSubject schoolYearSubject;
    // foreign key


}
