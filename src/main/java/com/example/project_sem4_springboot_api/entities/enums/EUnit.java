package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EUnit {
    BUOI("Buổi"),
    THANG("Tháng"),
    KY("Kỳ"),
    NAM("Năm");

    @Getter
    public final String unit;
}
