package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long indexLession;
    private Long timeOfDay;
    private Date dayOfWeek;
    private Date releaseAt;
    private String note;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "school_year_class_id")
    private SchoolYearClass schoolYearClass;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Attendance> attendances;


}
