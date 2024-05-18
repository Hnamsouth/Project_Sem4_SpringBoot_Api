package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto extends RegisterRequest {
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
