package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.TeacherContactDetail;
import com.example.project_sem4_springboot_api.entities.enums.TeacherType;
import com.example.project_sem4_springboot_api.entities.response.SubjectRes;
import com.example.project_sem4_springboot_api.entities.response.TeacherClassSubject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher_schoolyear_class_subject")
public class TeacherSchoolYearClassSubject {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schoolyear_class_id")
    @JsonManagedReference
    private SchoolYearClass schoolYearClass;
    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_id")
    @JsonManagedReference
    private TeacherSchoolYear teacherSchoolYear;

    @ManyToOne
    @JoinColumn(name = "schoolyear_subject_id")
    @JsonManagedReference
    private SchoolYearSubject schoolYearSubject;

    @OneToMany(mappedBy = "teacherSchoolYearClassSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<HomeWork> homeWorkList;



    // foreign key
    @JsonIgnore
    public TeacherContactDetail toContact (){
        boolean checkType =this.schoolYearClass.getTeacherSchoolYear() != null &&
                this.schoolYearClass.getTeacherSchoolYear().equals(this.teacherSchoolYear);
        var teacher = this.teacherSchoolYear.getTeacher();
        return TeacherContactDetail.builder()
                .teacherSchoolYearId(this.teacherSchoolYear.getId())
                .name(teacher.getSortName())
                .email(teacher.getUser().getUserDetail().get(0).getEmail())
                .phone(teacher.getUser().getUserDetail().get(0).getPhone())
                .subjects(Set.of(this.schoolYearSubject.getSubject().getName()))
                .teacherType(checkType ? TeacherType.GV_CHU_NHIEM:TeacherType.GV_BO_MON)
                .build();
    }

    @JsonIgnore
    public Map<String,Object> toTeacherHomeWork(){

        return Map.ofEntries(
                Map.entry("id",id),
                Map.entry("teacher",teacherSchoolYear.getTeacher().toTeacherInfo()),
                Map.entry("subject",schoolYearSubject.toRes())
        );
    }

    @JsonIgnore
    public TeacherClassSubject toRes(){
        var teacher = this.toContact();
        teacher.setSubjects(null);
        return TeacherClassSubject.builder()
                .id(this.id)
                .teacher(teacher)
                .subject(
                        SubjectRes.builder()
                                .id(this.getSchoolYearSubject().getId())
                                .name(this.getSchoolYearSubject().getSubject().getName())
                                .build()
                )
                .build();
    }

}
