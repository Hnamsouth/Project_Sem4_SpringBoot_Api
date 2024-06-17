package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AttendanceStatus {
    NGHI_CO_PHEP("Nghỉ có phép"),
    NGHI_KHONG_PHEP("Nghỉ không phép"),
    CO_MAT("Có mặt"),
    ;
    @Getter
    private final String name;
}
