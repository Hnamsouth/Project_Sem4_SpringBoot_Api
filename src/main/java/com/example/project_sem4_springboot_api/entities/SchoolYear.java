package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school_year")
public class SchoolYear {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startSem1;

    private Date startSem2;

    private Date end;

    // foreign key
    @OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<SchoolYearClass> schoolYearClass;

    @OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<TeacherSchoolYear> teacherSchoolYears;

    @OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<SchoolYearSubject> schoolYearSubjects;

}
