package com.example.project_sem4_springboot_api.dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentDto {

    private Long id;
    private boolean gender;
    @Size(min = 3, message = "FIRSTNAME_INVALID")
    private String firstName;
    @Size(min = 3, message = "LASTNAME_INVALID")
    private String lastName;
    private String email;
    private Date birthday;
    private String address;
    private int status;
    private String studentCode;

}
