package com.example.project_sem4_springboot_api.entities.request;
import jakarta.validation.constraints.Min;
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
public class TeacherSchoolYearCreate {

    private Long teacherId;
    private List<Long> teacherIds;
    @NotNull(message = "SchoolYear is required")
    @Min(value = 1, message = "SchoolYear must be greater than 0")
    private Long schoolYearId;
}
