package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fee_period")
public class FeePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private boolean status;
    private String statusCode;

    private Date endDate;
    private Date createdAt;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "school_year_id")
    private SchoolYear schoolyear;

    // foreign key
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "school_year_fee_period",
            joinColumns = @JoinColumn(name = "fee_period_id"),
            inverseJoinColumns = @JoinColumn(name = "school_year_fee_id")
    )
    private List<SchoolYearFee> schoolyearfees ;

    @JsonManagedReference
    @OneToMany(mappedBy = "feePeriod", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<FeePeriodScope> feePeriodScopes;

    @JsonManagedReference
    @OneToMany(mappedBy = "feePeriod", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<SchoolYearFeePeriod> schoolYearFeePeriods;

    @JsonManagedReference
    @OneToMany(mappedBy = "feePeriod", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<StudentTransaction> studentTransactions;

    @JsonIgnore
    public FeePeriod toResStudent(){
        return FeePeriod.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .statusCode(this.statusCode)
                .createdAt(this.createdAt)
                .endDate(this.endDate)
                .build();
    }



}
