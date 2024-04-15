package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.SchoolYear;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class CalendarReleaseResponse {
    private Long id;
    private String title;
    private Date releaseAt;
    private SchoolYear schoolYear;
    private List<ScheduleResponse> schedules;
}
