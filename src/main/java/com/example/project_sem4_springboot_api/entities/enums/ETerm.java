package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ETerm {
    THANG("Tháng"),
    NAM("Năm")
    ;
    @Getter
    public final String term;
}
