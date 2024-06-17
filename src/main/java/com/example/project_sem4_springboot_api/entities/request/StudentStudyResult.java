package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.EAcademicPerformance;
import com.example.project_sem4_springboot_api.entities.enums.EAchievement;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class StudentStudyResult {

    private Long id;
    @NotNull(message = "Số ngày nghỉ không được để trống!!!")
    @Min(value = 0, message = "Số ngày nghỉ không được nhỏ hơn 0!!!")
    private int numOfDayOff;
    private String note;
    @NotNull(message = "Học lực không được để trống!!!")
    @Enumerated(EnumType.STRING)
    private EAchievement academicPerformance; // học lực
    @NotNull(message = "Hạnh kiểm không được để trống!!!")
    @Enumerated(EnumType.STRING)
    private EAchievement conduct; // hạnh kiểm

    @Enumerated(EnumType.STRING)
    private EAcademicPerformance academicAchievement; // danh hiệu

    private boolean isPassed;
    @NotNull(message = "Id học sinh không được để trống!!!")

    private Long studentYearInfoId;
    @Valid
    private List<StudyResultScores> studyResultScores;
}
