package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class ScheduleRes {

    @Enumerated(EnumType.STRING)
    public DayOfWeek dayOfWeek;

    public List<ScheduleResponse> scheduleResponse ;
}
