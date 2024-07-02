package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_scores")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentScores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String score;
    private Date createdAt;
    private int semester;
    @Enumerated(EnumType.STRING)
    private ESem semesterName;

    @ManyToOne
    @JoinColumn(name = "point_type_id")
    @JsonManagedReference
    private PointType pointType;

    @ManyToOne
    @JoinColumn(name = "student_score_subject_id")
    @JsonManagedReference
    private StudentScoreSubject studentScoreSubject;

    @JsonIgnore
    public Map<String,Object> toRes(){
        return Map.of(
                "id",this.id,
                "score",this.score,
                "semester",this.semester,
                "semesterName",this.semesterName,
                "pointType",this.pointType.getPointType()
        );
    }

}
