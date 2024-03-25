package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int status;

    private String note;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student students;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

}
