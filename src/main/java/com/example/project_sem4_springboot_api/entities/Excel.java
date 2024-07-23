package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "excels")
@NoArgsConstructor
@AllArgsConstructor
public class Excel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private String description;
    private boolean published;


}
