package com.example.project_sem4_springboot_api.entities;

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
@Table(name = "school_year_fee_period")
public class SchoolYearFeePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private int amount;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "school_year_fee_id")
    private SchoolYearFee schoolyearfee;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "fee_period_id")
    private FeePeriod feePeriod;
}
