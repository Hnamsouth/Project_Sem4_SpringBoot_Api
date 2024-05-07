package com.example.project_sem4_springboot_api.entities.request;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceCreate {
    @NotNull(message = "Id thông tin học sinh không được để trống!!!")
    private Long studentYearInfoId;
//    private boolean status;
    private String note;

}
