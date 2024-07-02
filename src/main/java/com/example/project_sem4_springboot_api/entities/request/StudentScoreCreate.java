package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.ESem;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class StudentScoreCreate {

    @NotNull(message = "Id môn học không được để trống!!!")
    private Long schoolYearSubjectId;
    @NotNull(message = "Id lớp học không được để trống!!!")
    private Long schoolYearClassId;
    @Enumerated(EnumType.STRING)
    private ESem sem;
    @Valid
    private List<StudentScoreCreateDetail> studentScoreDetails;
}
