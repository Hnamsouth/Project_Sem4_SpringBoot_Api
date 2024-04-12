package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import com.example.project_sem4_springboot_api.entities.response.ScheduleResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ScheduleCreate {

    @NotNull(message = "teacher school year id is required")
    @Min(value = 1, message = "index lesson must be greater than 0")
    @Max(value = 4, message = "index lesson must be less than or equal 4")
    private int indexLesson;

    @NotNull(message = "study time is required")
    private StudyTime studyTime;

    @NotNull(message = "day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "teacher school year id is required")
    @FutureOrPresent(message = "release at must be future or present")
    private Date releaseAt;

    private String note;

    @NotNull(message = "teacher school year id is required")
    @Min(value = 1, message = "teacher school year id must be greater than 0")
    private Long teacherSchoolYearId;

    @NotNull(message = "school year class id is required")
    @Min(value = 1, message = "school year class id must be greater than 0")
    private Long schoolYearClassId;

    @NotNull(message = "school year subject id is required")
    @Min(value = 1, message = "school year subject id must be greater than 0")
    private Long schoolYearSubjectId;



}
