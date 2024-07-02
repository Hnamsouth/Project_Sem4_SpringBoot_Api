package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.EAcademicPerformance;
import com.example.project_sem4_springboot_api.entities.enums.EAchievement;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study_result_detail")
public class StudyResultDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String score;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "schoolyear_subject_id")
    @JsonManagedReference
    private SchoolYearSubject schoolYearSubject;
    @ManyToOne
    @JoinColumn(name = "study_result_id")
    @JsonManagedReference
    private StudyResult studyResult;

    @JsonIgnore
    public StudyResultDetail toRes(){
        return StudyResultDetail.builder()
                .id(this.id)
                .score(this.score)
                .createdAt(this.createdAt)
                .schoolYearSubject(this.schoolYearSubject.toRes())
                .build();
    }
}
