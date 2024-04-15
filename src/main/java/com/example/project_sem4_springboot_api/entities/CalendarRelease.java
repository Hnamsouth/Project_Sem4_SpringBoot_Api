package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.response.CalendarReleaseResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "calendar_release")
public class CalendarRelease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Date releaseAt;

    @ManyToOne
    @JoinColumn(name = "schoolyear_id")
    @JsonManagedReference
    private SchoolYear schoolYear;

    @OneToMany(mappedBy = "calendarRelease", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<Schedule> schedules;

    @JsonIgnore
    public CalendarReleaseResponse toResponse(){
        return CalendarReleaseResponse.builder().id(id).title(title).releaseAt(releaseAt).schoolYear(schoolYear)
                .schedules(schedules.stream().map(Schedule::toScheduleResponse).toList()).build();
    }

}
