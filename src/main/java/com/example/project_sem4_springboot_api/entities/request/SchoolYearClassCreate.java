package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class SchoolYearClassCreate {
    @NotBlank(message = "Class name is required")
    private String className;
    @NotBlank(message = "Class code is required")
    private String classCode;
    @NotNull(message = "Grade id is required")
    @Min(value = 1,message = "Grade id must be greater than 0")
    private Long gradeId;
    @NotNull(message = "Room id is required")
    @Min(value = 1,message = "Room id must be greater than 0")
    private Long roomId;
    private Long teacherSchoolYear;
    @NotNull(message = "School year is required")
    @Min(value = 1,message = "School year must be greater than 0")
    private Long schoolYear;
}
