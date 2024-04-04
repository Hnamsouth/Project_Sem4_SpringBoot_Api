package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schools")
public class School {
    //atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolName;

    private String schoolAddress;

    private String schoolEmail;

    private String schoolPhone;

    private String representativeName;

    private String representativeGender;
    // foreign key
    @OneToMany(mappedBy = "schoolId", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<SchoolService> schoolServices;

}
