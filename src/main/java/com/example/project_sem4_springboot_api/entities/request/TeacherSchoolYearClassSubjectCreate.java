package com.example.project_sem4_springboot_api.entities.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TeacherSchoolYearClassSubjectCreate {
    @NotNull(message = "TeacherSchoolYearId is required")
    @Min(value = 1, message = "TeacherSchoolYearId must be greater than 0")
    private Long teacherSchoolYearId;

    @NotNull(message = "SubjectClassList is required")
    private List<@NotNull SubjectClassCreate> subjectClassList;
}

