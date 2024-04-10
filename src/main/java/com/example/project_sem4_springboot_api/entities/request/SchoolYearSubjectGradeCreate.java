package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.ESem;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolYearSubjectGradeCreate {
    @NotNull(message = "GradeId is required")
    @Min(value = 1, message = "GradeId must be greater than 0")
    private Long gradeId;

    @NotNull(message = "SchoolYearSubjectId is required")
    @Min(value = 1, message = "SchoolYearSubjectId must be greater than 0")
    private Long schoolYearSubjectId;

    @NotNull(message = "Number is required")
    @Min(value = 1, message = "Number must be greater than 0")
    @Max(value = 999, message = "Number must be less than 999")
    private int number;

    @NotNull(message = "Sem is required")
    @Enumerated(value = EnumType.STRING)
    private ESem sem;
}
