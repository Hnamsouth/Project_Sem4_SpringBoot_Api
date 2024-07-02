package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.EPointType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScoreDetail {
    private Long studentScoreId;
    @NotBlank(message = "Điểm không được để trống!!!")
    private String score;
    @NotNull(message = "Loại điểm không được để trống!!!")
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private EPointType pointType;
}
