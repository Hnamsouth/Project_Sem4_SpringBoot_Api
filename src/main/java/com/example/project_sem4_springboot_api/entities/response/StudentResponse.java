package com.example.project_sem4_springboot_api.entities.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class StudentResponse {
    public Long studentYearInfoId;
    public String studentCode;
    public String fullName;
    public boolean gender;
    public Date birthday;
    public String className;
    public Long classId;
}
