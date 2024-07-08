package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.TeacherUpdateDto;
import com.example.project_sem4_springboot_api.dto.UserDto;
import com.example.project_sem4_springboot_api.entities.response.TeacherResponse;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @JsonManagedReference
    private User user;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<TeacherSchoolYear> teacherSchoolYears;

    @JsonIgnore
    public TeacherResponse toResponse(){
        return TeacherResponse.builder()
                .id(id)
                .officerNumber(officerNumber)
                .sortName(sortName)
                .joiningDate(joiningDate)
                .active(this.isActive())
                .user(user.getDto()).build();
    }
    @JsonIgnore
    public Teacher toResWithoutUser(){
        return Teacher.builder()
                .id(id)
                .officerNumber(officerNumber)
                .sortName(sortName)
                .joiningDate(joiningDate)
                .active(this.isActive())
                .build();
    }

    @JsonIgnore
    public Teacher from(TeacherUpdateDto data){
        this.setActive(data.isActive());
        this.setOfficerNumber(data.getOfficerNumber());
        this.setSortName(data.getSortName());
        this.setJoiningDate(data.getJoiningDate());
        return this;
    }

}
