package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.*;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentDto {

    private Long id;

    private String gender;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String address;
    private int status;
    private String studentCode;

}
