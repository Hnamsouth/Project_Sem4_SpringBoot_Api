package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school_services")
public class SchoolService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    private String description;

    @ManyToOne
    @JoinColumn(name = "school_services")
    @JsonManagedReference
    private School schoolId;


}
