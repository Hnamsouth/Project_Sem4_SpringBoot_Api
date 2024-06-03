package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.enums.AttendanceStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Builder
@Data
public class AttendanceRes {
    private Long id;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;
    private String attendanceStatusName;
    private String notificationStatus;
    private String note;
    private Date createdAt;
    private StudentResponse studentInfo;
}
