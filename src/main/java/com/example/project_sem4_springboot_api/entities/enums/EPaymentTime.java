package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EPaymentTime {
    HANG_THANG("Hàng tháng"),
    THANG_1("Tháng 1"),
    THANG_2("Tháng 2"),
    THANG_3("Tháng 3"),
    THANG_4("Tháng 4"),
    THANG_5("Tháng 5"),
    THANG_6("Tháng 6"),
    THANG_7("Tháng 7"),
    THANG_8("Tháng 8"),
    THANG_9("Tháng 9"),
    THANG_10("Tháng 10"),
    THANG_11("Tháng 11"),
    THANG_12("Tháng 12");

    @Getter
    public final String paymentTime;
}
