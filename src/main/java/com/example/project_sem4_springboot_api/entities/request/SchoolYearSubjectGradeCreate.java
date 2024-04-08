package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.ESem;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolYearSubjectGradeCreate {
    @NotNull
    private Long gradeId;

    @NotNull
    private Long schoolYearSubjectId;

    @NotNull
    @Size(min = 1, max = 999)
    private int number;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ESem sem;
}
