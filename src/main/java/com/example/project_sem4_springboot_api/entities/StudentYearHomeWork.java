package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_year_homework")
public class StudentYearHomeWork {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String url;
    private Date submitTime;
    private boolean status;
    private String statusName;
    private double point;
    @ManyToOne
    @JoinColumn(name = "student_year_info_id")
    @JsonManagedReference
    private StudentYearInfo studentYearInfo;
    @ManyToOne
    @JoinColumn(name = "homework_id")
    @JsonManagedReference
    private HomeWork homeWork;

    public Long getHomeWork_id() {
        return homeWork.getId();
    }

    @JsonIgnore
    public StudentYearHomeWorkDto convertToDto() {
        return StudentYearHomeWorkDto.builder()
                .id(this.id)
                .description(this.description)
                .url(this.url)
                .submitTime(this.submitTime)
                .status(this.status)
                .statusName(this.statusName)
                .point(this.point)
                .studentYearInfoId(this.studentYearInfo.toRes())
                .build();
    }
    @JsonIgnore
    public StudentYearHomeWorkDto convertToDtoNoStdInfo() {
        return StudentYearHomeWorkDto.builder()
                .id(this.id)
                .description(this.description)
                .url(this.url)
                .submitTime(this.submitTime)
                .status(this.status)
                .statusName(this.statusName)
                .point(this.point)
                .build();
    }


}