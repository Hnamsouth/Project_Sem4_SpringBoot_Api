package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SubjectPointType {
    DIEM_SO(true),
    DIEM_CHU(false);
    @Getter
    private final boolean pointType;
}
