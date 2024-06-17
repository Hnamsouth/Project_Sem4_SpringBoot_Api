package com.example.project_sem4_springboot_api.entities.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScheduleRes {

    public ScheduleResponse T2;
    public ScheduleResponse T3;
    public ScheduleResponse T4;
    public ScheduleResponse T5;
    public ScheduleResponse T6;
}
