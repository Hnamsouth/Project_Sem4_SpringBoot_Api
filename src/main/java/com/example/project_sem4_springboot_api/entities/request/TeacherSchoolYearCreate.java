package com.example.project_sem4_springboot_api.entities.request;
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

    @NotNull(message = "Id giáo viên không được để trống!!!")
    private List<Long> teacherIds;
    @NotNull(message = "Id năm học không được để trống")
    private Long schoolYearId;
}
