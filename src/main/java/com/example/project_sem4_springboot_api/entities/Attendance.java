package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int status;

    private String note;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonManagedReference
    private Student students;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonManagedReference
    private Schedule schedule;

}
