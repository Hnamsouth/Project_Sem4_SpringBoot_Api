package com.example.project_sem4_springboot_api.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class TeacherDetailsDto {

    private String officerNumber;
    private Long positionId;
    private Date joiningDate;
    private Long id;
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
