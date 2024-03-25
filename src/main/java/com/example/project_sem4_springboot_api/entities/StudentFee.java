package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "student_fee")
public class StudentFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean status;

    private Long paid;

    private Long received;

    @ManyToOne
    @JoinColumn(name = "student_year_info_id")
    private StudentYearInfo studentYearInfo;

    @ManyToOne
    @JoinColumn(name = "fee_id")
    private Fee fee;

}
