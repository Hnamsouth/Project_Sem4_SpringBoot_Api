package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Builder
@Data
public class TakeLeaveRequest {
    @NotNull(message = "Id học sinh không được để trống")
    private Long studentYearInfoId;
    @NotNull(message = "Id người gửi không được để trống")
    private Long userId;
    @NotBlank(message = "Lý do không được để trống")
    private String note;
    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;
    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

}
