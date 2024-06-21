package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.EPointType;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.response.StudentScoreSubjectRes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.stream.Stream;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_score_subject")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentScoreSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private String note;
    @ManyToOne
    @JoinColumn(name = "student_year_info_id")
    @JsonManagedReference
    private StudentYearInfo studentYearInfo;

    @ManyToOne
    @JoinColumn(name = "schoolyear_subject_id")
    @JsonManagedReference
    private SchoolYearSubject schoolYearSubject;

    @ManyToOne
    @JoinColumn(name = "teacher_schoolyear_id")
    @JsonManagedReference
    private TeacherSchoolYear teacherSchoolYear;

    // foregin key

    @OneToMany(mappedBy = "studentScoreSubject", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudentScores> studentScores;

    @JsonIgnore
    public StudentScoreSubject toRes(ESem semesterName){
        return StudentScoreSubject.builder()
                .id(this.id)
                .createdAt(this.createdAt)
                .note(this.note)
                .studentYearInfo(this.studentYearInfo.toRes())
                .schoolYearSubject(this.schoolYearSubject.toRes())
                .studentScores(this.studentScores.stream().filter(e->e.getSemesterName().equals(semesterName)).toList())
                .build();
    }

    @JsonIgnore
    public Map<String,Object> toResE(ESem semesterName){
        var sts = this.getStudentScores().stream().filter(e->e.getPointType().getPointType().equals(EPointType.DTB)).toList();
        Map<String,Object> stds = new HashMap<>();
        Arrays.stream(EPointType.values()).forEach(e->{
            stds.put(e.toString(),
                    this.studentScores.stream().filter(s->s.getSemesterName().equals(semesterName) &&
                    s.getPointType().getPointType().equals(e)
            ).map(StudentScores::toRes).toList());
        });

        Map<String,Object> ss = new TreeMap<>();
        ss.put("id",this.getId());
        ss.put("createdAt",this.getCreatedAt());
        ss.put("note",this.getNote());
        ss.put("semesterName",semesterName);
        ss.put("semester",semesterName.getSem()==3? "Cả năm":"Học kỳ "+semesterName.getSem());
        ss.put("studentYearInfo",this.getStudentYearInfo().toStudentResponse());
        ss.put("status",!sts.isEmpty() ? "Đã hoàn tất":"Chưa hoàn tất");
        ss.put("studentScores",stds);
            return ss;
    }

    @JsonIgnore
    public Map<String,Object> toResForStudent(ESem semesterName){
        Map<String,Object> stds = new HashMap<>();
        Arrays.stream(EPointType.values()).forEach(e->{
            stds.put(e.toString(),
                    this.studentScores.stream().filter(s->s.getSemesterName().equals(semesterName) &&
                            s.getPointType().getPointType().equals(e)
                    ).map(StudentScores::toRes).toList());
        });

        Map<String,Object> res = new HashMap<>();
        res.put("schoolYearSubject",this.getSchoolYearSubject().getSubject().toRes());
        res.put("studentScores",stds);
        return res;
    }

}
