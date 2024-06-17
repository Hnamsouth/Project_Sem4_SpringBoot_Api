package com.example.project_sem4_springboot_api.entities;


import com.example.project_sem4_springboot_api.entities.enums.EScope;
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
@Table(name = "transaction_detail")
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String title;
    private String description;
    private int amount;
    private double price;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "student_transaction_id")
    private StudentTransaction studentTransaction;
}
