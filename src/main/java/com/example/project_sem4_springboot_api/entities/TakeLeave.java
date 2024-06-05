package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.HandleStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "take_leave")
public class TakeLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private HandleStatus status;
    private String statusName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User parent;

    @ManyToOne
    @JoinColumn(name = "student_year_info_id")
    @JsonManagedReference
    private StudentYearInfo studentYearInfo;

}
