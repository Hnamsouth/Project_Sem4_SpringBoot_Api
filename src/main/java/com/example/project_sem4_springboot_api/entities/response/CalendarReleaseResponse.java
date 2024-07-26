package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.SchoolYear;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import jakarta.persistence.Enumerated;
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
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private ESem sem;
    private SchoolYear schoolYear;
    private List<ScheduleResponse> schedules;
}
