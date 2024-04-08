package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min=1)
    private Long grade;
    @NotNull
    @Size(min=1)
    private Long room;
    private Long teacherSchoolYear;
    @NotNull
    @Size(min=1)
    private Long schoolYear;
}
