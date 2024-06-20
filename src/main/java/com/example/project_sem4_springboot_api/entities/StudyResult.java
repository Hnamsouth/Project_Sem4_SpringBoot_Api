package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.EAcademicPerformance;
import com.example.project_sem4_springboot_api.entities.enums.EAchievement;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study_result")
public class StudyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numOfDayOff;
    private Date createdAt;
    private String note;
    private double averageScore;
    @Enumerated(EnumType.STRING)
    private ESem semester;
    private String semesterName;
    @Enumerated(EnumType.STRING)
    private EAchievement academicPerformance; // học lực
    @Enumerated(EnumType.STRING)
    private EAchievement conduct; // hạnh kiểm
    @Enumerated(EnumType.STRING)
    private EAcademicPerformance academicAchievement; // danh hiệu
    private boolean isPassed;
    private boolean isFinished;
    @ManyToOne
    @JoinColumn(name = "student_year_info_id")
    @JsonManagedReference
    private StudentYearInfo studentYearInfo;

    // foregin key
    @OneToMany(mappedBy = "studyResult", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudyResultDetail> studyResultDetails;


    @JsonIgnore
    public Map<String,Object> toRes(){
        Map<String,Object> res = new HashMap<>();
        res.put("id",this.id);
        res.put("numOfDayOff",this.numOfDayOff);
        res.put("createdAt",this.createdAt);
        res.put("note",this.note);
        res.put("averageScore",this.averageScore);
        res.put("semester",this.semester);
        res.put("semesterName",this.semesterName);
        res.put("academicPerformance",this.academicPerformance);
        res.put("conduct",this.conduct);
        res.put("academicAchievement",this.academicAchievement);
        res.put("isPassed",this.isPassed);
        res.put("isFinished",this.isFinished);
//        res.put("studentYearInfo",this.studentYearInfo.toRes());
        studyResultDetails.forEach(sr->{
            res.put(sr.getSchoolYearSubject().getSubject().getCode(),sr.toRes());
        });
        return res;
    }

}
