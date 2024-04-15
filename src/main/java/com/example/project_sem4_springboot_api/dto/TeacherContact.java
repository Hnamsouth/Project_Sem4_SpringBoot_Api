package com.example.project_sem4_springboot_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TeacherContact {
    private List<TeacherContactDetail> teacherContactDetails;
}
