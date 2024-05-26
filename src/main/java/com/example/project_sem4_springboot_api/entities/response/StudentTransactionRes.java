package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.StudentTransaction;

public class StudentTransactionRes extends StudentTransaction {
    public StudentResponse studentInfo;

    @Override
    public StudentTransaction toResponse() {
        return super.toResponse();
    }
}
