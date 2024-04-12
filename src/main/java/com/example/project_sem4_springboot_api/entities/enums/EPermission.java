package com.example.project_sem4_springboot_api.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EPermission {
    CREATE_USER("create:user"),
    UPDATE_USER("update:user"),
    READ_USER("read:user"),
    DELETE_USER("delete:user"),
    ALL_USER("all:user"),

    CREATE_STUDENT("create:student"),
    UPDATE_STUDENT("update:student"),
    READ_STUDENT("read:student"),
    DELETE_STUDENT("delete:student"),
    ALL_STUDENT("all:student"),

    CREATE_TEACHER("create:teacher"),
    UPDATE_TEACHER("update:teacher"),
    READ_TEACHER("read:teacher"),
    DELETE_TEACHER("delete:teacher"),
    ALL_TEACHER("all:teacher"),

    CREATE_SCHEDULE("create:schedule"),
    UPDATE_SCHEDULE("update:schedule"),
    READ_SCHEDULE("read:schedule"),
    DELETE_SCHEDULE("delete:schedule"),
    ALL_SCHEDULE("all:schedule"),
    ;
    // ... HOI DONG NHA TRUONG
    @Getter
    private final String permission;
}
