package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolYearFeePeriodCreate {
    @NotNull(message = "Số lượng Khoản thu không được để trống")
    public int amount;
    @NotNull(message = "Id Khoản thu không được để trống")
    public Long schoolYearFeeId;
}
