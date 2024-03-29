package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "test_point")
public class TestPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private float point;

    @ManyToOne
    @JoinColumn(name = "testPoints")
    private StudentYearInfo studentYearInfo;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "point_type_id")
    private PointType pointType;

}
