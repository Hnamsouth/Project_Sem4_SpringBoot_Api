package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gender;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String address;
    private int status;
    private String studentCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_parent",
            joinColumns = @JoinColumn(name="student_id"),
            inverseJoinColumns = @JoinColumn(name="parent_id")
    )
    private List<Parent> parents;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private List<StudentYearInfo> studentYearInfos;


    public StudentDto getDto(){
        StudentDto studentDto = new StudentDto();
        studentDto.setId(id);
        studentDto.setGender(gender);
        studentDto.setFirstName(firstName);
        studentDto.setLastName(lastName);
        studentDto.setBirthday(birthday);
        studentDto.setAddress(address);
        studentDto.setStatus(status);
        studentDto.setStudentCode(studentCode);
        return studentDto;
    }

}
