package com.example.project_sem4_springboot_api.entities.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ERole {
    ROLE_GV("giao_vien"), // GIAO VIEN
    ROLE_BGH("ban_giam_hieu"), // BAN GIAM HIEU
    ROLE_PH("phu_huynh"), // PHU HUYNH
    ROLE_DEV("dev"),
    ROLE_NV("nhan_vien") // NHAN VIEN
    ;
    @Getter
    private final String role;
}
