package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.ETerm;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school_year_fee")
public class SchoolYearFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private ETerm term;
    private String termName;
    private boolean compel;
    private boolean status;
    private boolean refund;
    private boolean exemption;

    @ManyToOne
    @JoinColumn(name = "payment_time_id")
    @JsonManagedReference
    private PaymentTime paymentTime;

    @ManyToOne
    @JoinColumn(name = "school_year_id")
    @JsonManagedReference
    private SchoolYear schoolyear;

    // foreign key
    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "schoolyearfees")
    private List<FeePeriod> feePeriods;

    @OneToMany(mappedBy = "schoolyearfee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FeePrice> feePrices;
}
