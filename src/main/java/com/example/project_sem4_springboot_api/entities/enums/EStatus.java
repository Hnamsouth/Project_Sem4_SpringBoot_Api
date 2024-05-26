package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EStatus {
    STUDENT_DANG_HOC("Đang học"),
    STUDENT_BO_HOC("Bỏ học"),
    STUDENT_THOI_HOC("Thôi học"), // nghỉ học vĩnh viễn
    STUDENT_TOT_NGHIEP("Tốt nghiệp"),
    STUDENT_BAO_LUU("Bảo lưu"),
    STUDENT_CHUYEN_TRUONG("Chuyển trường"),
    STUDENT_CHUYEN_LOP("Chuyển lớp"),
    STUDENT_TRANS_UNPAID("Chưa thanh toán"),
    STUDENT_TRANS_PAID("Đã thanh toán"),
    NGHI_TAM_THOI("Nghỉ tạm thời"),
    HOAT_DONG("Hoạt động"),
    NGUNG_HOAT_DONG("Ngừng hoạt động"),
    ;
    @Getter
    private final String name;
}
