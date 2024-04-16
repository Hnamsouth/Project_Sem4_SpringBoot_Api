package com.example.project_sem4_springboot_api.exception;

public class StudentAlreadyExistsException extends RuntimeException{

    public StudentAlreadyExistsException(String message){
        super(message);
    }

}


