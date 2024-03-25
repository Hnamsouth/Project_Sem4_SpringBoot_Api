package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "schools")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolName;

    private String schoolAddress;

    private String schoolEmail;

    private String schoolPhone;

    private String representativeName;

    private String representativeGender;

    @OneToMany(mappedBy = "schoolId", cascade = CascadeType.ALL)
    private List<SchoolService> schoolServices;

}
