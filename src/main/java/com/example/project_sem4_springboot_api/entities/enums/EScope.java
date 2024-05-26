package com.example.project_sem4_springboot_api.entities.enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EScope {
    SCHOOL("Toàn trường"),
    CLASS("Lớp"),
    GRADE("Khối");

    @Getter
    public final String scope;
}
