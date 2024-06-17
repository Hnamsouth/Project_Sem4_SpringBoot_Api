package com.example.project_sem4_springboot_api.entities.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EPointType {
    KTTX("Kiểm tra thường xuyên"),
    KT_GIUA_KY("Kiểm tra giữa kỳ"),
    KT_CUOI_KY("Kiểm tra cuối kỳ"),
    DTB("Điểm trung bình")
    ;
    @Getter
    private final String pointType;

}
