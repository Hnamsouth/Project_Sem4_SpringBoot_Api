package com.example.project_sem4_springboot_api.entities.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EAchievement {
    T("Tốt"),
    K("Khá"),
    TB("Trung bình"),
    Y("Yếu"),
    ;
    @Getter
    private final String name;

}
