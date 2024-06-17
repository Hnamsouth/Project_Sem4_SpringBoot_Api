package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class StudentScoreCreateDetail {
    @NotNull(message = "Id học sinh không được để trống!!!")
    private Long studentYearInfoId;
    @Valid
    private List<ScoreDetail> scoreDetails;
}
