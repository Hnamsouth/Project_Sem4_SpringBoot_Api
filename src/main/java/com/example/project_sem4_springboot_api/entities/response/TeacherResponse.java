package com.example.project_sem4_springboot_api.entities.response;


import com.example.project_sem4_springboot_api.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class TeacherResponse {
    private Long id;
    private String officerNumber;
    private String sortName;
    private Date joiningDate;
    private boolean active;
    private UserDto user;
}
