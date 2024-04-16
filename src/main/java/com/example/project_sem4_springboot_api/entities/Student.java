package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    private String email;
    private LocalDate birthday;
    private String address;
    private int status;
    private String studentCode;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_parent",
            joinColumns = @JoinColumn(name="student_id"),
            inverseJoinColumns = @JoinColumn(name="parent_id")
    )
    private List<Parent> parents;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<StudentYearInfo> studentYearInfos;

    @JsonIgnore
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
