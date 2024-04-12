package com.example.project_sem4_springboot_api.dto;


import com.example.project_sem4_springboot_api.entities.enums.TeacherType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class TeacherContactDetail {
    public Long teacherSchoolYearId;
    public String name;
    public String email;
    public String phone;
    public Set<String> subjects;
    @Enumerated(EnumType.STRING)
    public TeacherType teacherType;

}
