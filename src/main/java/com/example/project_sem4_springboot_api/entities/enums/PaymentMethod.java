package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {
    TIEN_MAT("Tiền mặt"),
    CHUYEN_KHOAN("Chuyển khoản");

    @Getter
    public final String paymentMethod;
}
