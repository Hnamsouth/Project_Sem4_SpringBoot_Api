package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    @JsonManagedReference
    private SchoolYear schoolYear;
    // foreign key
    @OneToMany(mappedBy = "schoolYearSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<TeacherSchoolYearClassSubject> teacherSchoolYearClassSubjects;

    @OneToMany(mappedBy = "schoolYearSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "schoolYearSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<SchoolYearSubjectGrade> schoolYearSubjectGrades;

    @OneToMany(mappedBy = "schoolYearSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<TestPoint> testPoints;

    @JsonIgnore
    public SchoolYearSubject toRes() {
        return SchoolYearSubject.builder()
                .id(this.id)
                .subject(this.subject)
                .build();
    }
}
