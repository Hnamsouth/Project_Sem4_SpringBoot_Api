package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StudyTime {
    SANG(1),
    CHIEU(2),
    TOI(3)
    ;
    @Getter
    private final int studyTime;
}
