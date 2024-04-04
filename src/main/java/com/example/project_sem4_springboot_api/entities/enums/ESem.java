package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ESem {
    HOC_KI_1(1),
    HOC_KI_2(2),
    CA_NAM(3)
    ;
    @Getter
    private final int sem;

}
