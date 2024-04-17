package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EStudentStatus {
    STUDENT_DANG_HOC("Đang học"),
    STUDENT_BO_HOC("Bỏ học"),
    STUDENT_THOI_HOC("Thôi học"), // nghỉ học vĩnh viễn
    STUDENT_TOT_NGHIEP("Tốt nghiệp"),
    STUDENT_BAO_LUU("Bảo lưu"),
    STUDENT_CHUYEN_TRUONG("Chuyển trường"),
    STUDENT_CHUYEN_LOP("Chuyển lớp"),
    STUDENT_NGHI_HOC_TAM_THOI("Nghỉ học tạm thời"),
    ;
    @Getter
    private final String name;
}
