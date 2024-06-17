package com.example.project_sem4_springboot_api.entities.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EAcademicPerformance {
    HSTT("Học sinh tiên tiến"),
    HSG("Học sinh giỏi"),
    ;
    @Getter
    private final String name;

}
