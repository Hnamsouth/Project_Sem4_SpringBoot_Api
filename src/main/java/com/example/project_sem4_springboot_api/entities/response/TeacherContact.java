package com.example.project_sem4_springboot_api.entities.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherContact {
    private Long id;
    private String shortName;
    private String name;
    private boolean gender;
    private String email;
    private String phone;
}
