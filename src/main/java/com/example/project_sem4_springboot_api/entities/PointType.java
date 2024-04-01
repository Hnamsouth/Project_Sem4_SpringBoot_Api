package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
