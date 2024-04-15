package com.example.project_sem4_springboot_api.entities.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CalendarReleaseUpdate {

    @NotBlank(message = "Tiêu đề không được để trống!!!")
    private String title;
    @NotNull(message = "Id Năm học không được để trống!!!")
    @Min(1)
    private Long schoolYearId;
    @NotNull(message = "Ngày Áp dụng Tkb không được để trống!!!")
    private Date releaseAt;
    @NotNull(message = "Id Đợt áp dụng không được để trống!!!")
    private Long id;
}
