package com.example.project_sem4_springboot_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolYearClassDto {
    @NotNull
    private String className;
    @NotNull
    private String classCode;
    @NotNull
    private Long grade;
    @NotNull
    private Long room;
    private Long teacherSchoolYear;
    @NotNull
    private Long schoolYear;
}
