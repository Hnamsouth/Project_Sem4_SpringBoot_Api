package com.example.project_sem4_springboot_api.entities.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EDeviceType {
    ANDROID("ANDROID"),
    IOS("IOS"),
    WEB("WEB");
    ;
    @Getter
    private final String name;

}
