package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "student_transaction")
public class StudentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionCode;
    private double total;
    private double paid;
    private String status;
    @Enumerated(EnumType.STRING)
    private EStatus statusCode;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "student_year_info_id")
    private StudentYearInfo studentYearInfo;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "fee_period_id")
    private FeePeriod feePeriod;

    // foreign key
    @JsonManagedReference
    @OneToMany(mappedBy = "studentTransaction", cascade = CascadeType.ALL)
    private List<TransactionDetail> transactionDetails;

    @JsonIgnore
    public StudentTransaction toResponse(){
        return StudentTransaction.builder()
                .id(this.id)
                .transactionCode(this.transactionCode)
                .total(this.total)
                .paid(this.paid)
                .status(this.status)
                .statusCode(this.statusCode)
//                .studentYearInfo(this.studentYearInfo)
                .feePeriod(this.feePeriod.toResStudent())
                .transactionDetails(this.transactionDetails)
                .build();
    }
}
