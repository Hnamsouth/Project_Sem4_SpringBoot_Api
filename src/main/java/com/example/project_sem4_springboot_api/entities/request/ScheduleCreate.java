package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ScheduleCreate {
    @NotNull(message = "Id Đợt áp dụng không được để trống!!!")
    public Long calendarReleaseId;

    @NotNull(message = "Id Đợt áp dụng không được để trống!!!")
    public Long classId;

    @NotNull(message = "Danh sách tkb không được để trống!!!")
    public Set<ScheduleDetailCreate> scheduleDetailCreate;
}
