package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.TeacherDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
public class Teacher {
    // atb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String officerNumber;
    private String sortName;
    private Date joiningDate;
    private boolean active;

    private Long positionId;


    // foreign key
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_department",
            joinColumns = @JoinColumn(name="teacher_id"),
            inverseJoinColumns = @JoinColumn(name="department_id")
    )
    private List<Department> departments;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TeacherSchoolYear> teacherSchoolYears;

    public TeacherDto getDto(){
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(id);
        teacherDto.setActive(active);
        teacherDto.setOfficerNumber(officerNumber);
        teacherDto.setJoiningDate(joiningDate);
        return teacherDto;
    }



}
