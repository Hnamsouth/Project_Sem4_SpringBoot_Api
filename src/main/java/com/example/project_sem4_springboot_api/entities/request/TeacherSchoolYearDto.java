package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.SchoolYear;
import com.example.project_sem4_springboot_api.entities.Teacher;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSchoolYearDto {

    private Long teacher;
    private List<Long> teachers;
    @NotNull
    private Long schoolYear;
}
