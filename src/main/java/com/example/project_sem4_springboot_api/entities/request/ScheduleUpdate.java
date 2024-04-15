package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleUpdate {

    @NotNull(message = "Id Tkb không được để trống!!!")
    private Long Id;

    @NotNull(message = "Tiết học không được để trống!!!")
    @Min(value = 1, message = "Vị trí tiết học phải lớn hơn 0 và nhỏ hơn hoặc bằng 4")
    @Max(value = 4, message = "Vị trí tiết học phải lớn hơn 0 và nhỏ hơn hoặc bằng 4")
    private int indexLesson;

    @NotNull(message = "Thời gian học không được để trống!!!")
    private StudyTime studyTime;

    @NotNull(message = "Ngày học trong tuần không được để trống!!!")
    private DayOfWeek dayOfWeek;

    private String note;

    @NotNull(message = "Id Giáo viên không được để trống!!! ")
    private Long teacherSchoolYearId;

    @NotNull(message = "Id Lớp học không được để trống!!!")
    private Long schoolYearClassId;

    @NotNull(message = "Id Môn học không được để trống!!!")
    private Long schoolYearSubjectId;

}
