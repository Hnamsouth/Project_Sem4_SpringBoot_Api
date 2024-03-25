package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String type;

    private String name;

    private Integer number;

    @ManyToMany(mappedBy = "subjects")
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<TestPoint> testPoints;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

}
