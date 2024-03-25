package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "point_type")
public class PointType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long coefficient;

    @OneToMany(mappedBy = "pointType", cascade = CascadeType.ALL)
    private List<TestPoint> testPoints;

}
