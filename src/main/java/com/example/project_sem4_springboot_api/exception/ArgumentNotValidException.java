package com.example.project_sem4_springboot_api.exception;

public class ArgumentNotValidException extends RuntimeException {

    private final String fieldName;
    private final String value;

    private final String message;


    @Override
    public String getMessage() {
        return message+" : "+fieldName+" = "+value;
    }

    public ArgumentNotValidException(String message, String fieldName, String value) {
        this.fieldName=fieldName;
        this.value=value;
        this.message=message;
    }

}
