package com.example.project_sem4_springboot_api.entities.request;
import com.example.project_sem4_springboot_api.entities.enums.AttendanceStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceCreateBody {
    private Long studentYearInfoId;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
}
