package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserNotificationActionType {
    STUDENT_ATTENDANCE("/student-attendance"),
    STUDENT_TAKE_LEAVE("/student-take-leave"),
    STUDENT_TAKE_LEAVE_APPROVAL("/student-take-leave"),
    STUDENT_PAYMENT("/student-payment"),
    STUDENT_PAYMENT_SUCCESS("/student-payment"),
    STUDENT_PAYMENT_REMINDER("/student-payment"),
    STUDENT_NOT_PAID("/student-not-paid"),
    STUDENT_RESULT("/student-result"),
    SCHEDULE("/schedule"),
    ;
    @Getter
    private final String routerPath;
}
