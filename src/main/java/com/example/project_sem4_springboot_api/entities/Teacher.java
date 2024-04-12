package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.TeacherDto;
import com.example.project_sem4_springboot_api.entities.response.TeacherContact;
import com.fasterxml.jackson.annotation.*;
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
    @JsonManagedReference
    private User user;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<TeacherSchoolYear> teacherSchoolYears;

    @JsonIgnore
    public TeacherContact getContact(){
        var userDetail = user.getUserDetail().get(0);
        return TeacherContact.builder()
                .phone(userDetail.getPhone())
                .email(userDetail.getEmail())
                .name(userDetail.getFirstname()+" "+userDetail.getLastname())
                .sortName(sortName)
                .build();
    }




}
