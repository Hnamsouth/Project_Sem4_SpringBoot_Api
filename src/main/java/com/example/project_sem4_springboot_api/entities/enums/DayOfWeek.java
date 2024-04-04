package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DayOfWeek {
    T2("thu_2"),
    T3("thu_3"),
    T4("thu_4"),
    T5("thu_5"),
    T6("thu_6"),
    T7("thu_7"),
    CN("chu_nhat"),
    ;
    @Getter
    private final String dayOfWeek;
}
