package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EPermission {
    GV_CN("gv:chu_nhiem"), // CHU NHIEM
    GV_BM("gv:bo_mon"), // BO MOM
    NV_TC("nv:tai_chinh"), // TAI CHINH
    NV_TS("nv:tuyen_sinh"), // TUYEN SINH
    NV_TV("nv:thu_vien"), // THU VIEN
    NV_VT("nv:van_thu"), // VAN THU
    BGH_HT("bgh:hieu_truong"), // HIEU TRUONG
    BGH_HP("bgh:hieu_pho")
    ;
    // ... HOI DONG NHA TRUONG
    @Getter
    private final String permission;
}
