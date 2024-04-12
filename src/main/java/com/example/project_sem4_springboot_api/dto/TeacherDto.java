package com.example.project_sem4_springboot_api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TeacherDto {

    private Long id;
    private String sortName;
    private String username;
    private String password;
    private String officerNumber;
    private Date joiningDate;
    private boolean active;
    private Long positionId;

    private String firstname;
    private String lastname;
    private String address;
    private String phone;
    private String email;
    private boolean gender;
    private Date birthday;
    private String citizen_id;
    private String nation;
    private String avatar;

}
