package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "fee")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long price;

    private int term;

    private String object;

    @OneToMany(mappedBy = "fee", cascade = CascadeType.ALL)
    private List<StudentFee> studentFees;

}
