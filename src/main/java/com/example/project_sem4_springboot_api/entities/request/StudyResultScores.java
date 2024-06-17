package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudyResultScores {
    private Long id;
    @NotNull(message = "Id môn học không được để trống!!!")
    private Long schoolYearSubjectId;
    @NotBlank(message = "Điểm không được để trống!!!")
    private String score;
}
