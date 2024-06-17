package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.EGrade;
import com.example.project_sem4_springboot_api.entities.enums.EUnit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fee_price")
public class FeePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double price;
    private Long gradeId;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "unit_id")
    private Unit unit;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "school_year_fee_id")
    private SchoolYearFee schoolyearfee;

    // foreign key

}
