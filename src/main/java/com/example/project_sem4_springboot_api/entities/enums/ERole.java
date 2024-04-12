package com.example.project_sem4_springboot_api.entities.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ERole {
    ROLE_GV("giao_vien"), // GIAO VIEN
    ROLE_BGH("ban_giam_hieu"), // BAN GIAM HIEU
    ROLE_PH("phu_huynh"), // PHU HUYNH
    ROLE_DEV("dev"),
    ROLE_NV_TC("nv:tai_chinh"), // NHAN VIEN
    ROLE_NV_TV("nv:thu_vien"), // NHAN VIEN
    ROLE_NV_VT("nv:van_thu"), // NHAN VIEN
    ;
    @Getter
    private final String role;
}
