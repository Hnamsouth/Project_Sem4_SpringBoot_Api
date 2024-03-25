package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "parents")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;
    private String address;
    private String gender;
    private String email;

    @ManyToMany(mappedBy = "parents")
    private List<Student> students;




}
