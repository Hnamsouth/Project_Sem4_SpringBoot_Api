package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schoolyear_class")
public class SchoolYearClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String className;
    @Column(nullable = false)
    private String classCode;

    private int grade;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL)
    private List<StudentYearInfo> studentYearInfos;

    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    private SchoolYear schoolYear;

    @ManyToOne
    @JoinColumn(name = "teacher_id",nullable = true)
    private Teacher teacher;

    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

}
