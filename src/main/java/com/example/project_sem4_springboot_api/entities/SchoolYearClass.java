package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "schoolyear_class")
public class SchoolYearClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;

    private String classCode;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL)
    private List<StudentYearInfo> studentYearInfos;

    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    private SchoolYear schoolYear;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "schoolYearClass", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

}
