package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkDto;
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
@Table(name = "homework")
public class HomeWork {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String description;
    private String url;
    private Date dueDate;
    private boolean status;
    private String statusName;
    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_id")
    @JsonManagedReference
    private TeacherSchoolYear teacherSchoolYear;
    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_class_subject_id")
    @JsonManagedReference
    private TeacherSchoolYearClassSubject teacherSchoolYearClassSubject;
    @ManyToOne
    @JoinColumn(name = "schoolyear_subject_id")
    @JsonManagedReference
    private SchoolYearSubject schoolYearSubject;
    @ManyToOne
    @JoinColumn(name = "schoolyear_class_id")
    @JsonManagedReference
    private SchoolYearClass schoolYearClass;
    // foreign key
    @OneToMany(mappedBy = "homeWork", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<StudentYearHomeWork> studentYearHomeWorks;


    @JsonIgnore
    public HomeWorkDto convertToDto(Long studentYearInfo) {
        return HomeWorkDto.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .description(this.description)
                .status(this.status)
                .statusName(this.statusName)
                .url(this.url)
                .dueDate(this.dueDate)
                .submission(studentYearHomeWorks.stream().anyMatch(
                        s->s.getStudentYearInfo().getId().equals(studentYearInfo)))
                .build();
    }

}