package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.TeacherContactDetail;
import com.example.project_sem4_springboot_api.entities.enums.TeacherType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // foreign key

    @JsonIgnore
    public TeacherContactDetail toContact (){
        boolean checkType = this.schoolYearClass.getTeacherSchoolYear().equals(this.teacherSchoolYear);
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

}
