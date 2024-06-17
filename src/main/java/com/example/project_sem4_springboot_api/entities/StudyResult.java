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
        Arrays.stream(StudyResult.class.getFields()).forEach(f->{
            try {
                if(f.getName().equals("studyResultDetails")){
                    res.put(f.getName(),f.get(this));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        studyResultDetails.forEach(sr->{
            res.put(sr.getSchoolYearSubject().getSubject().getName(),sr.toRes());
        });
        return res;
    }

}
