package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.response.StudentResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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

    private Date createdAt;
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
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudentScoreSubject> studentScoreSubjects;

    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudentTransaction> studentTransactions;

    @OneToMany(mappedBy = "studentYearInfo", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudyResult> studentStudyResults;
    @JsonIgnore
    public StudentResponse toStudentResponse() {
        return StudentResponse.builder()
                .studentYearInfoId(this.id)
                .studentCode(this.students.getStudentCode())
                .fullName(this.students.getFirstName() + " " + this.students.getLastName())
                .gender(this.students.isGender())
                .classId(this.schoolYearClass.getId())
                .className(this.schoolYearClass.getClassName())
                .build();

    }

    @JsonIgnore
    public StudentYearInfo toRes(){
        return StudentYearInfo.builder()
                .id(this.id)
                .createdAt(this.createdAt)
                .students(this.students.toResInfo())
                .schoolYearClass(this.schoolYearClass.toRes())
                .build();
    }

}