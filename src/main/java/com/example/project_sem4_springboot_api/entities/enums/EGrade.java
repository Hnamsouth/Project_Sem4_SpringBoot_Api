package com.example.project_sem4_springboot_api.entities.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EGrade {
    KHOI_1(1),
    KHOI_2(2),
    KHOI_3(3),
    KHOI_4(4),
    KHOI_5(5),
    ;
    @Getter
    private final int grade;

}
