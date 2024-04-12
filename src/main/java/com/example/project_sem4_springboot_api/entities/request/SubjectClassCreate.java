package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SubjectClassCreate {
    @NotNull(message = "SchoolYearSubjectId is required")
    @Min(value = 1, message = "SchoolYearSubjectId must be greater than 0")
    private Long schoolYearSubjectId;

    @NotNull(message = "SchoolYearClassId is required")
    @Min(value = 1, message = "SchoolYearClassId must be greater than 0")
    private List<Long> schoolYearClassId;
}
