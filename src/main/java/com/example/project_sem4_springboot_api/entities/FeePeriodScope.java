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
@Table(name = "fee_period_scope")
public class FeePeriodScope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long objectId;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "scope_id")
    private Scope scope;

    @ManyToOne
    @JoinColumn(name = "fee_period_id")
    @JsonBackReference
    private FeePeriod feePeriod;
    // foreign key

}
