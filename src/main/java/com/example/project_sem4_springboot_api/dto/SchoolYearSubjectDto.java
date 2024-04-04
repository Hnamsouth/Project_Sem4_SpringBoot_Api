package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.SchoolYear;
import com.example.project_sem4_springboot_api.entities.Subject;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolYearSubjectDto {

    private Long subject;
    private List<Long> subjects;
    @NotNull
    private Long schoolYear;
}
