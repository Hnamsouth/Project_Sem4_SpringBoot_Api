package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.springframework.data.web.SortDefault;

import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean gender;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String address;
    private String studentCode;
    private Date createdAt;


    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "student_parent",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> parents;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudentYearInfo> studentYearInfos;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    @JsonIgnoreProperties("student")
    private List<StudentStatus> studentStatuses;



    @JsonIgnore
    public Student toResInfo(){
        Student student = this;
        var sts = studentStatuses.stream().sorted(Comparator.comparing(StudentStatus::getId).reversed()).toList();
        student.setStudentStatuses(List.of(sts.get(0)));
        return this;
    }

}
