package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "school_year")
public class SchoolYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startSem1;

    private Date startSem2;

    private Date end;

    @OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL)
    private List<SchoolYearClass> schoolYearClass;

}
