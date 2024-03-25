package com.example.project_sem4_springboot_api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TeacherDto {

    private Long id;

    private String officerNumber;

    private Date joiningDate;

    private boolean active;

}
