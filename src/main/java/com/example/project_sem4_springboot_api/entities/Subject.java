package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.ESubjectType;
import com.example.project_sem4_springboot_api.entities.enums.SubjectPointType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subjects")
public class Subject {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ESubjectType type;

    private boolean isNumberType;
    @Enumerated(EnumType.STRING)
    private SubjectPointType subjectPointType;
    // cách tinh điểm
    private String description;
    private String name;
    // foreign key

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<SchoolYearSubject> schoolYearSubjects;

    @JsonIgnore
    public Subject toRes(){
        return Subject.builder()
                .id(this.id)
                .code(this.code)
                .type(this.type)
                .isNumberType(this.isNumberType)
                .subjectPointType(this.subjectPointType)
                .description(this.description)
                .name(this.name)
                .build();
    }

}
